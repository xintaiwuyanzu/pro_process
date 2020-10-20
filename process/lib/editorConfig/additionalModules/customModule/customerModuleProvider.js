export default class customModule {
    constructor(palette) {
        palette.registerProvider(this)
    }

    getPaletteEntries() {
        return (entries) => {
            /**
             * 删除不支持的事件
             */
            delete entries['lasso-tool']
            delete entries['space-tool']
            delete entries['global-connect-tool']
            delete entries['create.group']
            delete entries['create.participant-expanded']
            delete entries['create.subprocess-expanded']
            delete entries['create.data-store']
            delete entries['create.data-object']
            return entries
        }
    }

}
customModule.$inject = ['palette']
