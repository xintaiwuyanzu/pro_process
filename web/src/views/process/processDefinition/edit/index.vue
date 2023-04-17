<template>
  <section v-loading="loading">
    <nac-info :title=" `${id?'编辑':'添加'}流程定义`">
      <el-form inline ref="form">
        <!--        <el-form-item label="历史版本" v-if="id">
                  <el-select v-model="type" style="width: 120px">
                    <el-option v-for="type in processType" :label="type.name" :key="type.type" :value="type.type"/>
                  </el-select>
                </el-form-item>-->
        <el-form-item label="流程类型" ref="type" required>
          <el-select v-model="type" style="width: 120px">
            <el-option v-for="type in processType" :label="type.name" :key="type.type" :value="type.type"/>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="checkAndSave">保存</el-button>
          <el-button type="info" @click="$router.back()">返回</el-button>
        </el-form-item>
      </el-form>
    </nac-info>
    <section class="index_main">
      <bpmn-editor ref="editor"/>
    </section>
  </section>
</template>
<script>
import processTypeMixin from "../processTypeMixin";

/**
 * TODO 历史流程，流程示例
 * 流程定义编辑界面
 */
export default {
  mixins: [processTypeMixin],
  data() {
    return {
      loading: false,
      //流程定义Id
      //id: '',
      type: 'default_process',
      //流程定义对象
      processDefinition: {}
    }
  },
  methods: {
    /**
     * 获取流程定义基本信息
     * @returns {Promise<void>}
     */
    async loadDefinition() {
      this.loading = true
      const detailResult = await this.$post('/processDefinition/detail', {processDefinitionId: this.id})
      const data = detailResult.data
      if (data.success) {
        this.processDefinition = data.data
        this.type = this.processDefinition.type
        await this.loadXml()
      } else {
        this.$message.error(`获取流程信息失败：【${data.message}】`)
        this.$router.back()
      }
    },
    /**
     * 获取流程xml信息并加载到编辑器
     * @returns {Promise<void>}
     */
    async loadXml() {
      this.loading = true
      const xmlResult = await this.$post('/processDefinition/xml', {processDefinitionId: this.id})
      const data = xmlResult.data
      if (data.success) {
        await this.$refs.editor.loadXml(data.data)
        this.loading = false
      } else {
        this.$message.error(`获取流程定义文件失败：【${data.message}】`)
        this.$router.back()
      }
    },
    /**
     * 保存流程定义
     * @returns {Promise<void>}
     */
    async checkAndSave() {
      if (!this.type) {
        this.$message.error('请选择流程类型')
      } else {
        const xml = await this.$refs.editor.saveXml()
        const deployResult = await this.$post('/processDefinition/deploy', {id: this.id, type: this.type, xml: xml.xml})
        const data = deployResult.data
        if (data.success) {
          this.$message.success("保存成功！")
          this.$router.back()
        } else {
          this.$message.error(data.message)
        }
      }
    }
  },
  mounted() {
    //this.id = this.$route.query.id
    this.loadProcessType()
        .then(() => {
          if (this.id) {
            return this.loadDefinition()
          }
        })
  },
  computed:{
    //流程定义Id
    id(){
      return this.$route.query.id
    }
  }
}
</script>