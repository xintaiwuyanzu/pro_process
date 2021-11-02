module.exports = {
    __depends__: [
        require('bpmn-js-properties-panel/lib/provider/camunda/element-templates'),
    ],
    __init__: ['CamundaPropertiesProvider'],
    CamundaPropertiesProvider: ['type', require('bpmn-js-properties-panel/lib/provider/camunda/CamundaPropertiesProvider')]
};
