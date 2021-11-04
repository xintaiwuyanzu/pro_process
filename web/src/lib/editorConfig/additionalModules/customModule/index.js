import customerModuleProvider from "./customerModuleProvider";
import customerPropertiesProvider from './customerPropertiesProvider'

export default {
    __depends__: [require('./CamundaPropertiesProvider')],
    __init__: ['customerModuleProvider', 'propertiesProvider'],
    customerModuleProvider: ['type', customerModuleProvider],
    propertiesProvider: ['type', customerPropertiesProvider]
}
