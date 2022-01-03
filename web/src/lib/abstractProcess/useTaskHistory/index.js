import taskHistoryDialog from "./taskHistoryDialog";
import {useDialog} from "../useDialog";
import {http} from "@dr/framework/src/plugins/http";
import {Message} from 'element-ui'

/**
 * 流转历史弹窗
 */
export default () => {
    const {initDialog} = useDialog(taskHistoryDialog, dialog => {
        dialog.$on('close', () => {
            dialog.visible = false
        })
    })

    const showHistory = async (processInstanceId) => {
        const instance = initDialog()
        instance.tasks = []
        instance.visible = true
        instance.loading = true
        const {data} = await http().post('/processTaskInstance/history', {page: false, processInstanceId})
        if (data.success) {
            instance.tasks = data.data
            instance.loading = false
        } else {
            Message.warn('查询历史失败，请稍后重试')
        }
    }

    return {showHistory}
}