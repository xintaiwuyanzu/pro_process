<template>
  <section>
    <nac-info>
      <el-form :model="searchForm" inline ref="searchForm">
        <el-form-item label="流程类型" prop="type">
          <el-select style="width: 120px" v-model="searchForm.type" placeholder="请选择流程类型" filterable="true">
            <el-option v-for="type in processType" :label="type.name" :key="type.type" :value="type.type"/>
          </el-select>
        </el-form-item>
        <el-form-item label="流程名称" prop="name" style="padding-left: 10px">
          <el-input v-model="searchForm.name" style="width: 120px" placeholder="请输入流程名称"/>
        </el-form-item>
        <el-form-item>
          <el-button icon="el-icon-search" @click="loadData(searchForm)" type="primary">搜 索</el-button>
          <el-button type="info" @click="$refs.searchForm.resetFields()">重 置</el-button>
          <el-button icon="el-icon-plus" @click="edit()" type="primary">添 加</el-button>
          <el-button @click="start" type="primary">启动流程</el-button>
        </el-form-item>
      </el-form>
    </nac-info>
    <div class="index_main" v-loading="loading">
      <div class="table-container">
        <el-table :data="data" border height="100%" @selection-change="handleTableSelect">
          <el-table-column label="排序" type="index" align="center"/>
          <el-table-column label="流程名称" header-align="center" align="center" show-overflow-tooltip>
            <template slot-scope="scope">
              <el-button type="text" size="small" @click="edit(scope.row.id)">
                {{ scope.row.name }}
              </el-button>
            </template>
          </el-table-column>
          <el-table-column prop="key" label="流程编码" align="center" header-align="center" show-overflow-tooltip/>
          <el-table-column prop="description" label="流程描述" align="center" header-align="center"
                           show-overflow-tooltip/>
          <el-table-column label="流程类型" align="center" header-align="center" show-overflow-tooltip>
            <template slot-scope="scope">
              {{ getProcessType(scope.row.type) }}
            </template>
          </el-table-column>
          <el-table-column prop="version" label="最新版本" width="80px" align="center" header-align="center"/>
          <el-table-column label="操作" width="120" header-align="center" align="center">
            <template slot-scope="scope">
              <el-button type="text" @click="showDelete( scope.row.id)">删 除
              </el-button>
              <el-button type="text" @click="edit(scope.row.id)">
                编 辑
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <el-pagination
          @current-change="index=>loadData({pageIndex:index-1})"
          :current-page.sync="page.index"
          :page-size="page.size"
          layout="total, prev, pager, next"
          :total="page.total">
      </el-pagination>
    </div>
    <el-dialog title="提示" :visible.sync="deleteShow" width="40%">
      <span>此操作将删除选中数据, 是否继续?</span>
      <span slot="footer" class="dialog-footer">
         <el-button type="info" @click="deleteShow = false">取 消</el-button>
         <el-button type="danger" @click="doDelete(true)">删除所有版本</el-button>
         <el-button type="danger" @click="doDelete(false)">删除最新版本</el-button>
      </span>
    </el-dialog>
  </section>
</template>
<script>
import indexMixin from '@dr/auto/lib/util/indexMixin'
import processTypeMixin from "./processTypeMixin";
import abstractProcess from "../../../lib/abstractProcess";

export default {
  mixins: [indexMixin, processTypeMixin],
  extends: abstractProcess,
  data() {
    return {
      //流程定义类型
      path: '/processDefinition/',
      deleteShow: false,
      //暂存要删除的id
      deleteId: '',
      /**
       * 查询表单
       */
      searchForm: {
        type: '',
        name: ''
      }
    }
  },
  methods: {
    edit(id) {
      this.$router.push({path: this.$route.path + '/edit', query: {id}})
    },
    showDelete(id) {
      this.deleteId = id
      this.deleteShow = true
    },
    async doDelete(allVersion) {
      this.loading = true
      const result = await this.$post(this.apiPath() + '/delete', {id: this.deleteId, allVersion})
      const data = result.data
      if (data.success) {
        this.$message.success('删除成功！')
        await this.loadData()
      } else {
        this.$message.error(data.message)
      }
      this.loading = false
      this.deleteShow = false
    },
    async start() {
      const data = await this.startProcess('default_process', {})
      console.log(data)
    },
    async $init() {
      await this.loadProcessType()
      await this.loadData()
    }
  }
}
</script>
