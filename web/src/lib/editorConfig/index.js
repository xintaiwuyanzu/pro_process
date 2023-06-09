import viewConfig from '../viewConfig'
import template from './template'
import editAddition from './additionalModules'

export default {
    template,
    moddleExtensions: viewConfig.moddleExtensions,
    additionalModules: [...viewConfig.additionalModules, ...editAddition]
}
