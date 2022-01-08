import vue from 'vue'
import {Message} from 'element-ui'
import {http} from "@dr/framework/src/plugins/http";
import processSelect from "./processSelect";
import {useDialog} from "../useDialog";

/**
 * 启动流程方法
 */
export default () => {
    let resolve = null
    let params = null
    const resolveBack = (params) => {
        if (resolve) {
            resolve(params)
            resolve = null
        }
    }

    const {initDialog} = useDialog(processSelect, (dialog) => {
        dialog.$on('close', () => {
            dialog.visible = false
            resolveBack()
        })
        dialog.$on('submit', async (form) => {
            dialog.loading = true
            const {data} = await http().post('/processTaskInstance/start', {
                //流程定义Id
                definitionId: form.processId,
                //环节人员
                person: form.person.join(','),
                //审核意见
                comment: form.comment,
                //额外参数
                ...params
            })
            dialog.loading = false
            if (data.success) {
                //启动成功，回调状态
                dialog.visible = false
                resolve(form, data.data)
            } else {
                //启动失败
                Message.warning(data.message)
            }
        })
    })

    /**
     * 当前登录人启动特性类型的流程
     * @param processType 流程类型
     * @param addParams 额外的业务参数
     * @returns {Promise<void>}
     */
    const startProcess = async (processType, addParams) => {
        const {data} = await http().post('/processDefinition/userProcessDefinition', {processType})
        if (data.success) {
            const process = data.data
            const length = process.length
            if (length === 0) {
                Message.error('未查询到指定类型流程定义，请联系管理员设置并授权')
            } else {
                //只有一个流程定义
                //多个流程定义，弹框选择流程定义
                const instance = initDialog()
                instance.form = {processId: '', person: []}
                vue.nextTick(() => {
                    instance.processDefinition = process
                    instance.visible = true
                })
                params = addParams
                return new Promise((r) => {
                    //全局设置回调
                    resolve = r
                })
            }
        } else {
            Message.error(`查询流程定义失败【${data.message}】`)
        }
    }
    return {startProcess}
}