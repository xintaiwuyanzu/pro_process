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
            const startResult = await http().post('/taskInstance/start', {
                definitionId: form.processId
            })
            console.log(startResult)
            resolveBack(form)
        })
    })

    /**
     * 当前登录人启动特性类型的流程
     * @param processType
     * @returns {Promise<void>}
     */
    const startProcess = async (processType) => {
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