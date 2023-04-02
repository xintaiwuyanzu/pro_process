import TitleProps from "../../properties-panel-extension/provider/authority/parts/TitleProps";

const excludeArr = ['forms', 'process-variables']
export default class CustomerPropertiesProvider {
    constructor(CamundaPropertiesProvider) {
        this._provider = CamundaPropertiesProvider
    }

    newAuthorityTab(element) {
        return {
            id: 'authority', label: '权限', groups: createAuthorityTabGroups(element)
        };
    }

    getTabs(element) {
        //过滤掉无用的模块
        const tabs = this._provider.getTabs(element)
        console.log('tabs=', tabs)
        //todo 扩展 未实现
        tabs.push(this.newAuthorityTab(element))
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

function createAuthorityTabGroups(element) {
    const editAuthorityGroup = {
        id: 'edit-authority', label: '编辑权限', entries: []
    };

    // 每个属性都有自己的props方法
    TitleProps(editAuthorityGroup, element);
    // OtherProps1(editAuthorityGroup, element);
    // OtherProps2(editAuthorityGroup, element);

    return [editAuthorityGroup];
}

CustomerPropertiesProvider.$inject = ['CamundaPropertiesProvider']
