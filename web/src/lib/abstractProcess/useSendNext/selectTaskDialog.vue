<template>
  <el-dialog :title="title" :visible="visible"
             ref="dialog"
             width="60%"
             :close-on-click-modal="false"
             :close-on-press-escape="false"
             :show-close="false">
    <el-form :model="form" label-width="100px" ref="form" v-loading="loading">
      <el-form-item prop="taskDefinitionId" label="环节" required>
        <el-select v-model="form.taskDefinitionId" placeholder="请选择下一环节" filterable>
          <el-option v-for="define in taskDefinitions"
                     :value="define.id"
                     :label="`${define.name||define.id}`"
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
      <el-form-item prop="comment" label="意见">
        <el-input v-model="form.comment" placeholder="请填写意见" type="textarea"/>
      </el-form-item>
    </el-form>
    <section slot="footer">
      <el-button type="primary" @click="submit" :loading="loading">确 认</el-button>
      <el-button type="info" @click="$emit('close')" :loading="loading">取 消</el-button>
    </section>
  </el-dialog>
</template>

<script>
/**
 * 选择下一环节弹窗
 */
export default {
  name: "selectTaskDialog",
  props: {
    /**
     * 弹窗是否显示
     */
    visible: {type: Boolean, default: false},
    /**
     * 环节定义
     */
    taskDefinitions: {type: Array, default: () => []},
    /**
     * 加载状态
     */
    loading: {type: Boolean, default: false},
    /**
     * 弹窗标题
     */
    title: {type: String, default: '发送确认'}
  },
  data() {
    return {
      form: {
        //选择的环节定义Id
        taskDefinitionId: '',
        //选择的人员Id
        person: [],
        comment: ''
      },
      //当前登录人所属机构的所有人员
      currentPersons: []
    }
  },
  methods: {
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