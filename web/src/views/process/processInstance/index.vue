<template>
  <table-index path="processInstance"
               :edit="false"
               :insert="false"
               :delete="false"
               :fields="fields">
    <template slot="table-$btns" slot-scope="{row}">
      <el-button type="text" width="60" @click="()=>showHistory(row)">流转历史</el-button>
    </template>
    <circulation-history ref="circulationHistory" :processParams="processParams" v-loading="loading"
                         @doLoading="doLoading"/>
  </table-index>
</template>
<script>
import circulationHistory from "../circulationHistory";

/**
 * 已办任务
 */
export default {
  components: {circulationHistory},
  data() {
    return {
      fields: {
        createPersonName: {label: '任务发起人', search: true, width: 100},
        createDate: {dateFormat: true, label: '任务创建时间', width: 100},
        name: {label: '任务名称', search: true},
        type: {
          label: '任务类型',
          url: '/processDefinition/processType',
          labelKey: 'name',
          valueKey: 'type',
          width: 120,
          search: true,
          fieldType: 'select',
          filterable: true,
          requestMethod: 'get'
        },
        description: {label: '环节描述'}
      },
      processParams: {},
      loading: false
    }
  },
  methods: {
    async showHistory(row) {
      this.processParams = row
      this.processParams.processInstanceId = row.id
      await this.$refs.circulationHistory.openHistory()
    },
    doLoading(v) {
      this.loading = v
    }
  }
}
</script>
