<template>
  <table-index title="权限配置" path="processAuthority" :defaultSearchForm="defaultSearchForm"
               :edit="false"
               :insert="false"
               :delete="true"
               :fields="fields"
               ref="table">
    <template slot-scole='form' slot='search-$btns'>
      <el-button type="primary" @click="addShow=true">添加</el-button>
    </template>
    <el-dialog title="提示" :visible.sync="addShow" width="40%">
      <el-form :model="addForm" ref="form" label-width="100px">
        <el-form-item prop="selectPerson" label="选择角色" required>
          <el-select v-model="addForm.selectPerson" multiple filterable placeholder="请选择" style="width: 100%">
            <el-option
                v-for="item in roles"
                :key="item.id"
                :label="item.code+' '+item.name"
                :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
         <el-button type="info" @click="addShow = false">取 消</el-button>
         <el-button type="danger" @click="onsubmit">确定</el-button>
      </span>
    </el-dialog>
  </table-index>
</template>

<script>
export default {
  name: "index",
  data() {
    return {
      addShow: false,
      fields: {
        roleCode: {label: '角色编码', search: false, width: 200},
        roleName: {label: '角色名称', search: true},
        createDate: {dateFormat: "YYYY-MM-DD HH:mm:ss", label: '添加时间', width: 140}
      },
      addForm: {selectPerson: []},
      roles: [],
    }
  },
  methods: {
    $init() {
      this.getRoles()
    },
    defaultSearchForm() {
      return {
        processDefinitionId: this.processDefinitionId
      }
    },
    async getRoles() {
      const {data} = await this.$post('/sysrole/page', {
        page: false,
      })
      if (data.success) {
        this.roles = data.data
      }
    },
    async onsubmit() {
      this.addForm.roleId = this.addForm.selectPerson.join(',')
      const {data} = await this.$post('/processAuthority/insert', Object.assign(this.addForm, {processDefinitionId: this.processDefinitionId}))
      if (data.success) {
        this.$message.success('保存成功！')
        this.addShow = false
        this.$refs.table.reload(this.defaultSearchForm())
      }
    }
  },
  computed: {
    processDefinitionId() {
      return this.$route.query.id
    }
  }
}
</script>

<style scoped>

</style>