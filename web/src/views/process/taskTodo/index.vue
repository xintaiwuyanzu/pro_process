<template>
  <table-index path="processTaskInstance" :fields="fields" :edit="false" :insert="false" :delete="false"
               :defaultSearchForm="searchForm">
    <el-table-column align-="center" label="任务描述" prop="title" header-align="center">
      <template v-slot="{row}">
        <el-button type="text" @click="showDetail(row)">{{ row.title }}</el-button>
      </template>
    </el-table-column>
    <template slot="table-$btns" slot-scope="{row}">
      <el-button type="text" width="40" @click="showDetail(row)">审核</el-button>
      <el-button type="text" width="60" @click="()=>showHistory(row)">流转历史</el-button>
    </template>
    <circulation-history ref="circulationHistory" v-loading="loading" @doLoading="doLoading"
                         :processParams="processParams"/>
  </table-index>
</template>
<script>
import circulationHistory from "../circulationHistory";

/**
 * 待办任务首页
 */
export default {
  components: {circulationHistory},
  data() {
    return {
      searchForm: {withProcessVariables: true},
      fields: {
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
        title: {label: '任务标题', search: true},
        ownerName: {label: '任务发起人', search: true, width: 100},
        assigneeName: {label: '发送人', width: 100},
        createDate: {dateFormat: true, label: '发送时间', width: 100},
        name: {label: '当前环节名称', width: 140}
      },
      columns: {
        ownerName: {label: '任务发起人', search: true, width: 100},
        assigneeName: {label: '发送人', width: 100},
        createDate: {dateFormat: true, label: '发送时间', width: 100},
        name: {label: '环节定义名称', width: 160},
        title: {label: '任务描述'}
      },
      processParams: {},
      loading: false
    }
  },
  methods: {
    /**
     * 跳转详情页
     * @param row
     */
    showDetail(row) {
      let path = row.formUrl
      if (!path) {
        path = this.$route.path + "/detail"
        this.$message.warning('未配置详情页面，跳转默认页面')
      }
      const processVariables = row.processVariables ? row.processVariables : {}
      this.$router.push({
        path: path,
        query: {
          //流程实例Id
          processInstanceId: row.processInstanceId,
          //环节实例Id
          taskId: row.id,
          //批次Id
          batchId: processVariables.batchId,
          //业务外键
          businessId: processVariables.$businessId
        }
      })
    },
    async showHistory(row) {
      this.processParams = row
      await this.$refs.circulationHistory.openHistory()
    },
    doLoading(v) {
      this.loading = v
    }
  }
}
</script>