<template>
  <section class="bpmnContainer">
    <div class="bpmn-canvas" ref="canvas"/>
    <div class="bpmn-properties">
      <div class="properties-container" ref="properties"/>
    </div>
  </section>
</template>
<script>
import BpmnModeler from "bpmn-js/lib/Modeler";
import config from '../../../lib/editorConfig'


/**
 * 流程查看器
 */
export default {
  data() {
    return {
      bpmnModeler: null,
      // 这部分具体的代码我放到了下面
      initTemplate: config.template
    };
  },
  methods: {
    init() {
      // 创建Bpmn对象
      this.bpmnModeler = new BpmnModeler({
        container: this.$refs.canvas,
        propertiesPanel: {parent: this.$refs.properties},
        additionalModules: config.additionalModules,
        moddleExtensions: config.moddleExtensions
      });
      // 初始化建模器内容
      this.initDiagram(this.initTemplate);
    },
    initDiagram(bpmn) {
      // 将xml导入Bpmn-js建模器
      this.bpmnModeler.importXML(bpmn)
          .then(err => {
            if (err.length > 0) {
              this.$message.error("打开模型出错,请确认该模型符合Bpmn2.0规范");
            } else {
              this.bpmnModeler.get('canvas').zoom('fit-viewport')
            }
          });
    },
  },
  mounted() {
    this.init();
  }
}
</script>
<style lang="scss">
.bpmnContainer {
  height: 100%;
}
</style>
