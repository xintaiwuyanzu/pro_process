<template>
  <section style="display: inline-block">
    <slot>
      <el-button type="primary" @click="open">提交审核</el-button>
    </slot>
    <el-dialog :title="dialogTitle" ref="dialog"
               append-to-body
               :visible.sync="dialogVisible" width="60%" :close-on-click-modal="false" :close-on-press-escape="false"
               :show-close="false">
      <el-form :model="dialogForm" label-width="100px" ref="form" v-loading="dialogLoading">
        <el-form-item prop="processId" label="流程" required>
          <el-select v-model="dialogForm.processId" placeholder="请选择要发起的流程" filterable>
            <el-option v-for="define in processDefinition"
                       :value="define.id"
                       :label="`${define.name||define.key}-${define.version}`"
                       :key="define.id"/>
          </el-select>
        </el-form-item>
        <el-form-item prop="person" label="接收人" required>
          <el-select v-model="dialogForm.person" multiple placeholder="请选择接收人" filterable>
            <el-option v-for="person in currentPersons"
                       :value="person.id"
                       :label="person.userName"
                       :key="person.id"/>
          </el-select>
        </el-form-item>
        <slot :form="dialogForm" name="form"/>
        <el-form-item prop="comment" label="意见">
          <el-input v-model="dialogForm.comment" placeholder="请填写意见" type="textarea"/>
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
import {http} from "@dr/framework/src/plugins/http";

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
    businessId: {type: String}
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
        await this.beforeSave(this.dialogForm)
      }
      const form = this.dialogForm
      const {data} = await http().post('/processTaskInstance/start', {
        ...form,
        businessId: this.businessId,
        //流程定义Id
        definitionId: form.processId,
        //环节人员
        person: form.person.join(','),
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
      if (this.currentPersons) {
        const {data} = await this.$post('/processDefinition/currentOrganisePersons/')
        if (data.success) {
          this.currentPersons = data.data
        }
      }
      if (this.processDefinition.length === 0 || this.lastProcessType !== this.processType) {
        this.lastProcessType = this.processType
        //如果流程定义还未加载或者上次的流程类型与现在的流程类型不同，则重新加载流程数据
        const {data} = await http().post('/processDefinition/userProcessDefinition', {processType: this.processType})
        if (data.success) {
          this.processDefinition = data.data
        } else {
          this.processDefinition = []
        }
      }
    },
    $init() {
      this.defaultDialogTitle = '启动流程确认'
    }
  }
}
</script>
