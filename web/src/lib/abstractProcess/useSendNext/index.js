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
    let params = null

    const {initDialog} = useDialog(selectTaskDialog, {
        close() {
            this.loading = false
            this.visible = false
            if (resolve) {
                resolve()
            }
        },
        async submit(form) {
            if (resolve) {
                this.loading = true
                //调用后台发送方法
                const {data} = await http().post('/processTaskInstance/send', {
                    taskInstanceId,
                    $nextId: form.taskDefinitionId,
                    assignee: form.person.join(','),
                    comment: form.comment
                })
                resolve(data, form)
                this.loading = false
                this.visible = false
            }
        }
    })
    /**
     * 发送到下一环节
     * @param taskId 当前环节实例Id
     * @returns {Promise<unknown>}
     */
    const sendNext = async (taskId, query) => {
        if (query && query.complete) {
            //办结按钮
            const {data} = await http().post('/processTaskInstance/end', {
                taskInstanceId: taskId,
                comment: ''
            })
            return data
        } else {
            params = query
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
    }
    return {
        sendNext
    }
}