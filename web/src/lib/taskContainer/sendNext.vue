<template>
  <section>
    <el-button type="success" @click="open" v-loading="dialogLoading">发送</el-button>
    <el-dialog :title="dialogTitle" ref="dialog"
               append-to-body
               :visible.sync="dialogVisible" width="60%" :close-on-click-modal="false" :close-on-press-escape="false"
               :show-close="false">
      <el-form :model="dialogForm" label-width="100px" ref="form" v-loading="dialogLoading">
        <el-form-item prop="taskDefinitionId" label="环节" required>
          <el-select v-model="dialogForm.taskDefinitionId" placeholder="请选择下一环节" filterable>
            <el-option v-for="define in taskDefinitions"
                       :value="define.id"
                       :label="`${define.name||define.id}`"
                       :key="define.id"/>
          </el-select>
        </el-form-item>
        <el-form-item prop="person" label="接收人" required>
          <el-select v-model="dialogForm.person" placeholder="请选择接收人" filterable>
            <el-option v-for="person in persons"
                       :value="person.id"
                       :label="person.userName"
                       :key="person.id"/>
          </el-select>
        </el-form-item>
        <slot :form="dialogForm" name="sendForm"/>
        <el-form-item prop="comment" label="意见">
          <process-opinion v-model="dialogForm.comment"/>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button type="primary" @click="save" :loading="dialogLoading">确 认</el-button>
        <el-button type="info" @click="close" :loading="dialogLoading">取 消</el-button>
      </div>
    </el-dialog>
  </section>
</template>
<script>
import abstractTaskItem from "./abstractTaskItem";
import abstractDialog from '../abstractDialog'
import {http} from "@dr/framework/src/plugins/http";

/**
 * 发送按钮
 */
export default {
  mixins: [abstractTaskItem, abstractDialog],
  name: "sendNext",
  data() {
    return {
      //环节定义
      taskDefinitions: [],
      //人员定义
      persons: []
    }
  },
  methods: {
    /**
     * 执行发送方法
     * @returns {Promise<void>}
     */
    async save() {
      await this.$refs.form.validate()
      this.dialogLoading = true
      //保存前拦截
      if (this.beforeSave) {
        await this.beforeSave(this.dialogForm)
      }
      const form = this.dialogForm
      const {data} = await http().post('/processTaskInstance/send', {
        ...form,
        taskInstanceId: this.taskInstanceId,
        $nextId: form.taskDefinitionId,
        assignee: form.person,
        comment: form.comment
      })
      this.$emit('sendSaved', data)
      this.dialogLoading = false
      this.close()
    },
    /**
     * 弹窗打开前回调
     * @param form
     * @returns {Promise<void>}
     */
    async $initDialogForm() {
      if (this.taskInstanceId && this.taskDefinitions.length === 0) {
        const {data} = await this.$post('taskDefinition/nextTasks', {taskInstanceId: this.taskInstanceId})
        this.taskDefinitions = data.data
        if (this.taskDefinitions.length > 0) {
          this.$set(this.dialogForm, 'taskDefinitionId', this.taskDefinitions[0].id)
        }
      }
      if (this.persons.length === 0) {
        const {data} = await this.$post('/processDefinition/currentOrganisePersons/')
        this.persons = data.data
        if (this.persons.length > 0) {
          this.$set(this.dialogForm, 'person', this.persons[0].id)
        }
      }
    }
  },
  mounted() {
    this.defaultDialogTitle = '发送确认'
  }
}
</script>
