<template>
  <el-descriptions :title="history.name" :column="3" border style="margin-bottom: 8px;width: 100%">
    <el-descriptions-item label="审核人" :span="1">
      {{ history.assigneeName }}
    </el-descriptions-item>
    <el-descriptions-item label="审核时间" :span="1">
      {{ history.createDate|datetime }}
    </el-descriptions-item>
  </el-descriptions>
</template>

<script>
/**
 * 流转历史小项
 */
export default {
  name: "historyItem", props: {
    history: {type: Object, default: () => ({})}
  }
}
</script>
