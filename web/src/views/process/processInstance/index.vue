<template>
  <table-index path="processInstance"
               :edit="false"
               :insert="false"
               :delete="false"
               :fields="fields">
    <template v-slot:table-$btns="{row}">
      <el-button type="text" width="60" @click="()=>showHistory(row.id)">流转历史</el-button>
    </template>
  </table-index>
</template>
<script>
import abstractProcess from "../../../lib/abstractProcess";

/**
 * 流程实例
 */
export default {
  extends: abstractProcess,
  data() {
    return {
      fields: {
        createPersonName: {label: '任务发起人', search: true, width: 100},
        createDate: {dateFormat: true, label: '任务创建时间', width: 100},
        name: {label: '任务名称', search: true},
        type: {label: '任务类型'},
        description: {label: '环节描述'}
      }
    }
  }
}
</script>
