import viewConfig from './viewConfig'
import editorConfig from './editorConfig'
import container from './processContainer'
import task from './taskContainer'
import history from './taskHistoryContainer'

/**
 * 启动流程容器
 */
export const processContainer = container
/**
 * 环节按钮容器
 */
export const taskContainer = task
/**
 * 流转历史
 * @type {{data(): {loading: boolean, tasks: []}, watch: {processInstanceId(): void}, methods: {loadHistory(): Promise<void>, $init(): Promise<void>}, name: string, props: {processInstanceId: {type: String | StringConstructor}}}}
 */
export const taskHistoryContainer = history
/**
 * 这里统一出口，省的到处引用错误
 */
export default {
    Modeler: import('camunda-bpmn-js/lib/camunda-platform/Modeler'),
    viewConfig,
    editorConfig
}