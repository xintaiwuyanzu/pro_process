import Modeler from "camunda-bpmn-js/lib/camunda-platform/Modeler";
import viewConfig from './viewConfig'
import editorConfig from './editorConfig'

/**
 * 这里统一出口，省的到处引用错误
 */
export default {
    Modeler,
    viewConfig,
    editorConfig
}