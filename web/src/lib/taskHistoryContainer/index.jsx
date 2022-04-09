import HistoryItem from "./historyItem";

/**
 * 流转历史容器
 */
export default {
    name: 'taskHistoryContainer',
    components: {HistoryItem},
    props: {
        processInstanceId: {type: String}
    },
    data() {
        return {
            loading: false,
            tasks: []
        }
    },
    watch: {
        processInstanceId() {
            this.loadHistory()
        }
    },
    methods: {
        async loadHistory() {
            if (this.processInstanceId) {
                this.loading = true
                const {data} = await this.$post('/processTaskInstance/history', {
                    page: false,
                    processInstanceId: this.processInstanceId
                })
                this.tasks = data.data
                this.loading = false
            } else {
                this.tasks = []
            }
        },
        async $init() {
            await this.loadHistory()
        }
    }, render() {
        const children = this.tasks.map(history => {
            const scopedSlots = {
                description: () => {
                    if (this.$scopedSlots.default) {
                        return this.$scopedSlots.default({history})
                    } else {
                        return (<HistoryItem history={history}/>)
                    }
                }
            }
            return (<el-step scopedSlots={scopedSlots}/>)
        })
        return (
            <section>
                <el-steps direction="vertical">
                    {children}
                </el-steps>
            </section>
        )
    }
}