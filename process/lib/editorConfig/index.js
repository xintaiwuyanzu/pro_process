import viewConfig from '../viewConfig'
import editAddition from './additionalModules'
import "bpmn-js-properties-panel/dist/assets/bpmn-js-properties-panel.css";
import "diagram-js-minimap/assets/diagram-js-minimap.css";

export default {
    template: viewConfig.template,
    moddleExtensions: viewConfig.moddleExtensions,
    additionalModules: [].concat(editAddition).concat(viewConfig.additionalModules)
}
