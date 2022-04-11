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
            return this.taskContainer?.taskInstance || {}
        }
    },
    inject: {
        taskContainer: {default: () => undefined}
    }
}