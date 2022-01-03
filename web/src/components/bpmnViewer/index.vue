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
    async init() {
      const Modeler = await lib.Modeler
      // 创建Bpmn对象
      this.bpmnModeler = await new Modeler.default({
        container: this.$refs.canvas,
        additionalModules: lib.viewConfig.additionalModules,
        moddleExtensions: lib.viewConfig.moddleExtensions
      });
      // 初始化建模器内容
      await this.initDiagram(this.initTemplate);
    },
    async initDiagram(bpmn) {
      // 将xml导入Bpmn-js建模器
      const err = await this.bpmnModeler.importXML(bpmn)
      if (err.length > 0) {
        this.$message.error("打开模型出错,请确认该模型符合Bpmn2.0规范");
      } else {
        this.bpmnModeler.get('canvas').zoom('fit-viewport')
      }
    },
  },
  mounted() {
    this.init();
  }
}
</script>
