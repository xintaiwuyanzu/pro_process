import entryFactory from 'bpmn-js-properties-panel/lib/factory/EntryFactory';

import {
    is
} from 'bpmn-js/lib/util/ModelUtil';

import {http} from '@dr/framework/src/plugins/http'


export default async function(group, element) {
    const selectOptions = []
    const {data} = await http().post('/person/page', {"page": false})

    if(data.success){
        data.data.forEach(item => {
            selectOptions.push({'name': item.userCode + ' ' + item.userName, 'value': item.id})
        })
    }

    // for(let i = 0; i < 10; i++) {
    //     selectOptions.push({'name': `admin 超级管理员${i}`, 'value': `defaultadmin${i}`})
    // }
    console.log('selectOptions==',selectOptions)

    if (is(element, 'bpmn:Activity')) { // 可以在这里做类型判断
        group.entries.push(entryFactory.selectBox('', {
            id: 'authority',
            description: '请选择该节点审批人',
            label : '审批人',
            selectOptions: selectOptions,
            modelProperty : 'authority'
        }));
    }
}