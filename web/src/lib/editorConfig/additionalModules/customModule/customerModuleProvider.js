export default class customModule {
    constructor(palette) {
        palette.registerProvider(this)
    }

    getPaletteEntries() {
        return (entries) => {
            /**
             * 删除不支持的事件
             */
            //套索工具
            //delete entries['lasso-tool']
            //创建空间？？
            delete entries['space-tool']
            //全局链接器
            delete entries['global-connect-tool']
            //创建分组工具
            //delete entries['create.group']
            //创建事件网关 控件上的网关删除不掉？
            delete entries['create.exclusive-gateway']
            //发送事件
            delete entries['create.intermediate-event']

            delete entries['create.participant-expanded']
            delete entries['create.subprocess-expanded']
            delete entries['create.data-store']
            delete entries['create.data-object']
            return entries
        }
    }

}
customModule.$inject = ['palette']
