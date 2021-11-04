/**
 * 流程类型mixin
 */
export default {
    data() {
        return {
            processType: []
        }
    },
    methods: {
        /**
         * 加载流程定义类型
         * @returns {Promise<void>}
         */
        async loadProcessType() {
            const data = await this.$http.get('/processDefinition/processType')
            if (data.data.data) {
                this.processType = data.data.data
            }
        }, /**
         * 获取流程类型名称
         */
        getProcessType(type) {
            if (type && this.processType) {
                const find = this.processType.find(p => p.type === type)
                if (find) {
                    return find.name
                }
            }
            return type
        }
    }
}