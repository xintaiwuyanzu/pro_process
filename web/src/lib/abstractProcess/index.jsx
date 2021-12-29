import useStartProcess from "./useStartProcess";
import useTaskHistory from "./useTaskHistory";
import useProperties from "./useProperties";
import useSendNext from "./useSendNext";

/**
 * 这个流程相关按钮的集合，提供相关异步的操作方法
 * 使用的类只需要继承该类即可
 */
export default {
    setup() {
        //启动流程按钮
        const {startProcess} = useStartProcess()
        //发送
        const {sendNext} = useSendNext()
        //办结
        //回退
        //流转历史
        const {showHistory} = useTaskHistory()
        //流程和环节定义属性
        const {properties, loadProperties} = useProperties()

        return {
            //启动流程异步方法
            startProcess,
            //发送到下一环节
            sendNext,
            //显示流转历史弹窗
            showHistory,
            //当前环节，定义的属性
            properties,
            //加载流程或者环节定义属性异步方法
            loadProperties
        }
    }
}