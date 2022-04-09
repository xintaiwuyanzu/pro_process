/**
 * 环节控件基础父类
 */
export default {
    props: {
        /**
         *环节实例Id
         */
        taskInstanceId: {type: String}
    },
    data() {
        return {loading: false}
    },
    inject: {
        taskContainer: {default: () => undefined}
    }
}