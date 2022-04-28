export default {
    props: {
        /**
         *环节实例Id
         */
        taskInstanceId: {type: String}
    },
    provide() {
        //向子组件提供本组件注入
        return {taskContainer: this}
    },
    data() {
        return {
            //加载状态
            loading: false,
            //环节实例对象
            taskInstance: {}
        }
    },
    methods: {
        /**
         * 加载环节实例
         * @returns {Promise<void>}
         */
        async loadTaskInstance() {
            if (this.taskInstanceId && !this.loading) {
                this.loading = true
                const {data} = await this.$get('/processTaskInstance/detail?id=' + this.taskInstanceId)
                if (data.success) {
                    this.taskInstance = data.data
                }
                this.loading = false
            }
        },
        async $init() {
            await this.loadTaskInstance()
        }
    },
    watch: {
        async taskInstanceId() {
            //await this.loadTaskInstance()
        }
    }
}