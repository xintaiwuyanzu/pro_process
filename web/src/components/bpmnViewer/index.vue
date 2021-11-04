<template>
  <section class="bpmnContainer">
    <div class="bpmn-canvas" ref="canvas"/>
  </section>
</template>
<script>
import lib from "../../lib";


/**
 * 流程查看器
 */
export default {
  data() {
    return {
      bpmnModeler: null,
      // 这部分具体的代码我放到了下面
      initTemplate: lib.viewConfig.template
    };
  },
  methods: {
    init() {
      // 创建Bpmn对象
      this.bpmnModeler = new lib.Modeler({
        container: this.$refs.canvas,
        additionalModules: lib.viewConfig.additionalModules,
        moddleExtensions: lib.viewConfig.moddleExtensions
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
