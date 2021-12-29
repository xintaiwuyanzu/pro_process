<template>
  <el-dialog title="流程启动确认"
             ref="dialog"
             :visible.sync="visible"
             width="60%"
             :close-on-click-modal="false"
             :close-on-press-escape="false"
             :show-close="false">
    <el-form :model="form" label-width="100px" ref="form" v-loading="loading">
      <el-form-item prop="processId" label="流程" required>
        <el-select v-model="form.processId" placeholder="请选择要发起的流程" filterable>
          <el-option v-for="define in processDefinition"
                     :value="define.id"
                     :label="`${define.name||define.key}-${define.version}`"
                     :key="define.id"/>
        </el-select>
      </el-form-item>
      <el-form-item prop="person" label="接收人" required>
        <el-select v-model="form.person" multiple placeholder="请选择接收人" filterable>
          <el-option v-for="person in currentPersons"
                     :value="person.id"
                     :label="person.userName"
                     :key="person.id"/>
        </el-select>
      </el-form-item>
    </el-form>
    <div slot="footer">
      <el-button type="primary" @click="submit" :loading="loading">确 认</el-button>
      <el-button type="info" @click="$emit('close')" :loading="loading">取 消</el-button>
    </div>
  </el-dialog>
</template>
<script>
/**
 * 流程相关选择器
 */
export default {
  name: 'processSelect',
  props: {
    /**
     * 弹窗显示状态
     */
    visible: {default: false, type: Boolean},
    /**
     * 可选的流程定义
     */
    processDefinition: {type: Array, default: () => []},
    /**
     * 加载状态
     */
    loading: {type: Boolean, default: false}
  },
  data() {
    return {
      form: {
        processId: '',
        person: []
      },
      //当前登录人所属机构的所有人员
      currentPersons: []
    }
  },
  methods: {
    /**
     *提交表单
     * @returns {Promise<void>}
     */
    async submit() {
      const valid = await this.$refs.form.validate()
      if (valid) {
        this.$emit('submit', this.form)
      }
    },
    async $init() {
      const {data} = await this.$post('/processDefinition/currentOrganisePersons/')
      if (data.success) {
        this.currentPersons = data.data
      }
    }
  }
}
</script>