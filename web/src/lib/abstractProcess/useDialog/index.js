import vue from 'vue'
import {onBeforeUnmount} from 'vue-demi'

export const useDialog = (vueOptions, callback) => {
    let instance = null
    /**
     * 初始化dialog
     * @returns {null}
     */
    const initDialog = () => {
        if (!instance) {
            //根据option创建vue函数
            const Component = vue.extend(vueOptions)
            instance = new Component().$mount()
            document.body.appendChild(instance.$el)

            if (callback) {
                callback(instance)
            }
        }
        return instance
    }
    /**
     * 退出时删除element
     */
    onBeforeUnmount(() => {
        if (instance) {
            instance.visible = false
            document.body.removeChild(instance.$el)
            instance.$destroy()
        }
    })
    return {initDialog}
}