<template>
  <section style="display: inline-block">
    <slot>
      <el-button type="primary" @click="open" :loading="dialogLoading">{{ btnText }}</el-button>
    </slot>
    <el-dialog :title="dialogTitle" ref="dialog"
               append-to-body
               :visible.sync="dialogVisible" width="60%" :close-on-click-modal="false" :close-on-press-escape="false"
               :show-close="false">
      <el-form :model="dialogForm" label-width="100px" ref="form" v-loading="dialogLoading">
        <el-form-item prop="processId" label="流程" required>
          <el-select v-model="dialogForm.processId" placeholder="请选择要发起的流程" @change="getCurrentPersons" filterable
                     clearable>
            <el-option v-for="define in processDefinition"
                       :value="define.id"
                       :label="`${define.name||define.key}`"
                       :key="define.id"/>
          </el-select>
        </el-form-item>
        <el-form-item prop="person" label="接收人" required>
          <el-select v-model="dialogForm.person" placeholder="请选择接收人" filterable>
            <el-option v-for="person in currentPersons"
                       :value="person.id"
                       :label="person.userName"
                       :key="person.id"/>
          </el-select>
        </el-form-item>
        <slot :form="dialogForm" name="form"/>
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
<script>/**
 * 启动流程容器
 */
import abstractDialog from "../abstractDialog";

export default {
  name: 'processContainer',
  extends: abstractDialog,
  props: {
    /**
     * 流程类型
     */
    processType: {type: String},
    /**
     * 业务外键
     */
    businessId: {type: String},
    /**
     * 提交按钮显示名称
     */
    btnText: {type: String, default: '提交审核'}
  },
  data() {
    return {
      //流程定义
      processDefinition: [],
      //最后加载的流程定义类型
      lastProcessType: '',
      //当前所在机构登录人员
      currentPersons: []
    }
  },
  methods: {
    //启动流程
    async save() {
      await this.$refs.form.validate()
      this.dialogLoading = true
      //保存前拦截
      if (this.beforeSave) {
        const result = await this.beforeSave(this.dialogForm)
        if (result === false) {
          this.dialogLoading = false
          return
        }
      }
      const form = this.dialogForm
      const {data} = await this.$post('/processTaskInstance/start', {
        ...form,
        businessId: this.businessId,
        //流程定义Id
        definitionId: form.processId,
        //环节人员
        assignee: form.person,
        //审核意见
        comment: form.comment
      })
      this.$emit('saved', data)
      this.dialogLoading = false
      this.close()
    },
    /**
     * 弹窗打开回调
     * @returns {Promise<void>}
     */
    async $initDialogForm() {
      this.currentPersons = []
      //设置默认数据
      if (this.currentPersons.length > 0) {
        this.$set(this.dialogForm, 'person', this.currentPersons[0].id)
      }
      if (this.processDefinition.length === 0 || this.lastProcessType !== this.processType) {
        this.lastProcessType = this.processType
        //如果流程定义还未加载或者上次的流程类型与现在的流程类型不同，则重新加载流程数据
        const {data} = await this.$post('/processDefinition/userProcessDefinition', {
          processType: this.processType,
          useLatestVersion: true
        })
        if (data.success) {
          this.processDefinition = data.data
        } else {
          this.processDefinition = []
        }
      }
      //设置默认数据
      if (this.processDefinition.length > 0) {
        this.$set(this.dialogForm, 'processId', this.processDefinition[0].id)
      }
      await this.getCurrentPersons()
      if (this.processDefinition.length === 0) {
        this.$message.error("未找到指定类型的流程，请联系系统管理员分配权限")
      }
    },
    async $init() {
      this.defaultDialogTitle = '启动流程确认'
    },
    async getCurrentPersons() {
      await this.resetDialogForm()
      if (this.dialogForm.processId) {
        const {data} = await this.$post('/processAuthority/getPersonByRoleAndCurOrg', {processDefinitionId: this.dialogForm.processId})
        if (data.success) {
          this.currentPersons = data.data
        }
      }
    },
    async resetDialogForm() {
      this.currentPersons = []
      this.$set(this.dialogForm, 'person', [])
    },
  }
}
</script>
