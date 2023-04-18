import customerModuleProvider from "./customerModuleProvider";
import customerPropertiesProvider from './customerPropertiesProvider'
import customerPopMenuProvider from './customerPopMenuProvider'
import customContextPad from "./customContextPad";

export default {
    __depends__: [require('./CamundaPropertiesProvider')],
    __init__: ['customerModuleProvider', 'propertiesProvider', 'popMenuProvider', 'customContextPad'],
    customerModuleProvider: ['type', customerModuleProvider],
    propertiesProvider: ['type', customerPropertiesProvider],
    popMenuProvider: ['type', customerPopMenuProvider],
    customContextPad: ['type', customContextPad]
}
