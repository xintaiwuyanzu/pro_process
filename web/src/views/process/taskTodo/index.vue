<template>
  <table-index path="processTaskInstance" :fields="fields" :edit="false" :insert="false" :delete="false">
    <template slot="table-$btns" slot-scope="{row}">
      <el-button type="text" width="40" @click="showDetail(row)">查看</el-button>
      <el-button type="text" width="60" @click="()=>showHistory(row.processInstanceId)">流转历史</el-button>
    </template>
  </table-index>
</template>
<script>
import abstractProcess from "../../../lib/abstractProcess";

/**
 * 待办任务首页
 */
export default {
  extends: abstractProcess,
  data() {
    return {
      fields: {
        ownerName: {label: '任务发起人', search: true, width: 100},
        assigneeName: {label: '发送人', width: 100},
        createDate: {dateFormat: true, label: '发送时间', width: 100},
        name: {label: '当前环节名称', width: 160},
        title: {label: '任务名称'},
        process_TYPE: {label: '任务类型'},
        description: {label: '环节描述'}
      }
    }
  },
  methods: {
    /**
     * 跳转详情页
     * @param row
     */
    showDetail(row) {
      this.$router.push({
        path: this.$route.path + "/detail",
        query: {processInstanceId: row.processInstanceId, taskId: row.id}
      })
    }
  }
}
</script>