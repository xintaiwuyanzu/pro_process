import entryFactory from 'bpmn-js-properties-panel/lib/factory/EntryFactory';

import {
    is
} from 'bpmn-js/lib/util/ModelUtil';



export default async function(group, element) {
    if (is(element, 'bpmn:UserTask')) { // 可以在这里做类型判断
        group.entries.push(entryFactory.selectBox('', {
            id: 'authority',
            description: '请选择该节点审批人',
            label : '审批人',
            selectOptions: [{'name': '--请选择--', 'value': '0'}],
            modelProperty : 'authority'
        }));
    }
}