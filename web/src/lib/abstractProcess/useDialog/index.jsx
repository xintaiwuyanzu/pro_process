import vue, {getCurrentInstance, onBeforeUnmount} from 'vue'

export const useDialog = (vueOptions, callback, scopeName) => {
    let instance = null
    const {proxy} = getCurrentInstance()

    /**
     * 初始化dialog
     * @returns {null}
     */
    const initDialog = () => {
        if (!instance) {
            //根据option创建vue函数
            instance = new vue({
                inheritAttrs: false,
                props: vueOptions.props,
                render() {
                    const props = {...this.$props, ...this.$attrs}
                    const scopedSlots = {}
                    if (proxy[scopeName]) {
                        scopedSlots.default = proxy[scopeName]
                    }
                    const listeners = {}

                    const vm = this
                    Object.keys(callback).forEach(k => {
                        listeners[k] = function () {
                            callback[k].call(vm, ...arguments)
                        }
                    })
                    return (<vueOptions ref='dialog' props={props} on={listeners} scopedSlots={scopedSlots}/>)
                }
            })
            instance.$mount()
            document.body.appendChild(instance.$el)
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