/**
 * 环节控件基础父类
 */
export default {
    data() {
        return {loading: false}
    },
    computed: {
        //环节实例Id通过计算的来
        taskInstanceId() {
            return this.taskInstance.id || ''
        },
        taskInstance() {
            if (this.taskContainer && this.taskContainer.taskInstance) {
                return this.taskContainer.taskInstance
            } else {
                return {}
            }
        }
    },
    inject: {
        taskContainer: {default: () => undefined}
    }
}