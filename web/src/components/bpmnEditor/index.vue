<style lang="scss">
.bpmnContainer {
  display: flex;
  flex: 1;
  overflow: auto;
  border: $--border-base;

  .divider {
    height: 100%;
  }

  .bpmn-canvas {
    display: flex;
    flex: 1;
  }
}
</style>
<template>
  <section class="bpmnContainer">
    <div class="bpmn-canvas" ref="canvas"/>
    <el-divider direction="vertical" class="divider"/>
    <div class="bpmn-properties">
      <div class="properties-container" ref="properties"/>
    </div>
  </section>
</template>
<script>
import {editorConfig, Modeler} from "../../lib";
import Ids from 'ids'
import replaceIds from '@bpmn-io/replace-ids'

const ids = new Ids([32, 36, 1])

function generateId() {
  return ids.next();
}

/**
 * TODO 能够直接查看编辑xml
 * 流程查看器
 */
export default {
  data() {
    return {
      bpmnModeler: null,
      // 这部分具体的代码我放到了下面
      initTemplate: editorConfig.template
    };
  },
  methods: {
    async init() {
      const modelerConstruct = await Modeler
      // 创建Bpmn对象
      this.bpmnModeler = await new modelerConstruct.default({
        container: this.$refs.canvas,
        propertiesPanel: {parent: this.$refs.properties},
        additionalModules: editorConfig.additionalModules,
        moddleExtensions: editorConfig.moddleExtensions
      });
      // 初始化建模器内容
      await this.loadXml(this.initTemplate);
    },
    async loadXml(bpmn) {
      //替换模板中的Id
      const content = replaceIds(bpmn, generateId)
      // 将xml导入Bpmn-js建模器
      const err = await this.bpmnModeler.importXML(content)
      if (err.length > 0) {
        this.$message.error("打开模型出错,请确认该模型符合Bpmn2.0规范");
      } else {
        this.bpmnModeler.get('canvas').zoom('fit-viewport')
      }
    },
    /**
     * 保存xml文件
     * @returns {Promise<*>}
     */
    async saveXml() {
      const result = await this.bpmnModeler.saveXML()
      return result
    }
  },
  mounted() {
    this.init()
  }
}
</script>
