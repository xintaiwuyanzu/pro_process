<template>
  <el-dialog title="流转历史" width="80%" :visible.sync="visible">
    <table-render :columns="columns" :data="tasks" v-loading="loading" style="height: 300px;" :index="true"/>
    <div slot="footer">
      <el-button @click="()=>$emit('close')" type="primary">确 定</el-button>
    </div>
  </el-dialog>
</template>
<script>
export default {
  name: "taskHistoryDialog",
  props: {
    loading: {type: Boolean, default: false},
    /**
     * 历史任务列表
     */
    tasks: {type: Array, default: () => []},
    /**
     *显示状态
     */
    visible: {type: Boolean, default: false}
  },
  data() {
    return {
      columns: {
        ownerName: {label: '任务发起人', search: true, width: 100},
        assigneeName: {label: '发送人', width: 100},
        createDate: {dateFormat: true, label: '发送时间', width: 100},
        name: {label: '环节定义名称', width: 160},
        title: {label: '任务描述'}
      }
    }
  }
}
</script>