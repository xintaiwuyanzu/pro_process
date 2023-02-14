<template>
  <section>
    <el-button type="success" @click="open" v-loading="dialogLoading">退回</el-button>
    <el-dialog :title="dialogTitle" ref="dialog"
               append-to-body
               :visible.sync="dialogVisible" width="60%" :close-on-click-modal="false" :close-on-press-escape="false"
               :show-close="false">
      <el-form :model="dialogForm" label-width="100px" ref="form" v-loading="dialogLoading">
        <el-form-item prop="hisId" label="历史环节" required>
          <el-select v-model="dialogForm.hisId" placeholder="请选择历史环节" filterable>
            <el-option v-for="define in historys"
                       :value="define.id"
                       :label="`${define.name||define.id}【${define.ownerName}】`"
                       :key="define.id"/>
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
 * 退回按钮
 */
export default {
  mixins: [abstractTaskItem, abstractDialog],
  name: "sendBack",
  data() {
    return {
      //环节定义
      historys: []
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

      const his = this.historys.find(d => d.id === this.dialogForm.hisId)
      const {data} = await http().post('/processTaskInstance/send', {
        ...form,
        taskInstanceId: this.taskInstanceId,
        $nextId: his.taskDefineKey,
        assignee: his.owner,
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
      if (this.taskInstanceId && this.historys.length === 0) {
        const {data} = await this.$post('processTaskInstance/history', {
          processInstanceId: this.taskInstance.processInstanceId,
          page: false
        })
        this.historys = data.data.filter(d => d.id !== this.taskInstanceId)
        if (this.historys.length > 0) {
          this.$set(this.dialogForm, 'hisId', this.historys[0].id)
        }
      }
    }
  },
  mounted() {
    this.defaultDialogTitle = '退回确认'
  }
}
</script>
