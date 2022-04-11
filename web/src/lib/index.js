import viewConfig from './viewConfig'
import editorConfig from './editorConfig'
import processContainer from './processContainer'
import taskContainer, {endProcess, sendBack, sendNext} from './taskContainer'
import taskHistoryContainer from './taskHistoryContainer'

const Modeler = import('camunda-bpmn-js/lib/camunda-platform/Modeler')
/**
 * 这里统一出口，省的到处引用错误
 */
export {
    Modeler,

    viewConfig,
    editorConfig,
    //环节容器
    taskContainer,
    //退回环节按钮
    sendBack,
    //发送下一环节按钮
    sendNext,
    //办结流程按钮
    endProcess,
    //启动流程容器
    processContainer,
    //流程历史容器
    taskHistoryContainer
}