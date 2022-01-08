import selectTaskDialog from "./selectTaskDialog";
import {useDialog} from "../useDialog";
import {http} from "@dr/framework/src/plugins/http";

/**
 * 发送到下一环节
 */
export default () => {
    const loadTaskDefinition = async (taskInstanceId) => {
        const {data} = await http().post('taskDefinition/nextTasks', {taskInstanceId})
        return data
    }

    let resolve = null
    let taskInstanceId = null


    const {initDialog} = useDialog(selectTaskDialog, (instance) => {
        instance.$on('submit', async (form) => {
            if (resolve) {
                instance.loading = true
                //调用后台发送方法
                const {data} = await http().post('/processTaskInstance/send', {
                    taskInstanceId,
                    nextActivityId: form.taskDefinitionId,
                    nextPersonId: form.person.join(','),
                    comment: ''
                })
                resolve(data, form)
                instance.loading = false
                instance.visible = false
            }
        })
        instance.$on('close', () => {
            instance.loading = false
            instance.visible = false
            if (resolve) {
                resolve()
            }
        })
    })
    /**
     * 发送到下一环节
     * @param taskId 当前环节实例Id
     * @returns {Promise<unknown>}
     */
    const sendNext = async (taskId) => {
        taskInstanceId = taskId
        const instance = initDialog()
        instance.loading = true
        instance.visible = true
        //加载下一环节定义信息
        const data = await loadTaskDefinition(taskId)
        instance.taskDefinitions = data.data
        instance.loading = false
        return new Promise((r) => {
            resolve = r
        })
    }
    return {
        sendNext
    }
}