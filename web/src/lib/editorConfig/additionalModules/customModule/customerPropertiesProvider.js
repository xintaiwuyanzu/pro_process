const excludeArr = ['forms', 'process-variables']
export default class CustomerPropertiesProvider {
    constructor(CamundaPropertiesProvider) {
        this._provider = CamundaPropertiesProvider
    }

    getTabs(element) {
        //过滤掉无用的模块
        const tabs = this._provider.getTabs(element)
        tabs.forEach(({id, groups}) => {
            if (id === 'general') {
                const generalIndex = groups.findIndex(v => v.id === 'general')
                const documentationIndex = groups.findIndex(v => v.id === 'documentation')
                if (generalIndex > -1 && documentationIndex > -1) {
                    groups[generalIndex].entries = groups[generalIndex].entries.concat(groups[documentationIndex].entries)
                    groups.splice(generalIndex - 1, 1)
                }
            }
        })
        return tabs.filter(c => excludeArr.indexOf(c.id) < 0)
    }
}

CustomerPropertiesProvider.$inject = ['CamundaPropertiesProvider']
