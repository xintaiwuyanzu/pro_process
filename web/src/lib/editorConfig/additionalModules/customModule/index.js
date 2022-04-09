import customerModuleProvider from "./customerModuleProvider";
import customerPropertiesProvider from './customerPropertiesProvider'
import customerPopMenuProvider from './customerPopMenuProvider'

export default {
    __depends__: [require('./CamundaPropertiesProvider')],
    __init__: ['customerModuleProvider', 'propertiesProvider', 'popMenuProvider'],
    customerModuleProvider: ['type', customerModuleProvider],
    propertiesProvider: ['type', customerPropertiesProvider],
    popMenuProvider: ['type', customerPopMenuProvider]
}
