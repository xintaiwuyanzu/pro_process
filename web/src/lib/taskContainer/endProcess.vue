<template>
  <section>
    <el-button type="success" @click="open">办结</el-button>
    <el-dialog :title="dialogTitle" ref="dialog"
               append-to-body
               :visible.sync="dialogVisible" width="60%" :close-on-click-modal="false" :close-on-press-escape="false"
               :show-close="false">
      <el-form :model="dialogForm" label-width="100px" ref="form" v-loading="dialogLoading">
        <slot :form="dialogForm" name="sendForm"/>
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
<script>
/**
 * 办结按钮
 */
import abstractDialog from "../abstractDialog";
import abstractTaskItem from "./abstractTaskItem";

export default {
  name: "endProcess",
  mixins: [abstractTaskItem, abstractDialog],
  methods: {
    async save() {
      const {data} = await this.$post('/processTaskInstance/end', {
        taskInstanceId: this.taskInstanceId,
        ...this.dialogForm
      })
      this.$emit('saveEnd', data)
      this.dialogLoading = false
      this.close()
    },
    $init() {
      this.defaultDialogTitle = '办结流程'
    }
  }
}
</script>