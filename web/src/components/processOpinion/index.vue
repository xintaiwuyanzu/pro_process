<template>
  <section class="opinion_container">
    <el-input v-model="inputValue" placeholder="请填写意见" type="textarea"/>
    <el-button type="primary" @click="addOpinion" v-loading="loading">设为常用意见</el-button>
    <br/>
    <section v-loading="loading">
      <el-tag v-for="tag in opinions" :key="tag.id" closable v-if="opinions.length" @click="radioSelect(tag.opinion)"
              @close="remove(tag)">
        {{ tag.opinion }}
      </el-tag>
    </section>
  </section>
</template>

<script>
export default {
  name: "processOpinion",
  props: {
    //数据值
    value: String,
  },
  data() {
    return {
      opinions: [],
      inputValue: '',
      radioValue: '',
      loading: false
    }
  },
  watch: {
    value(v) {
      if (this.inputValue !== v) {
        this.inputValue = v
      }
    },
    inputValue(v) {
      if (v !== this.value) {
        this.$emit('input', v)
      }
    }
  },
  methods: {
    radioSelect(v) {
      this.inputValue = v
    },
    $init() {
      this.loadOpinion()
    },
    async loadOpinion() {
      this.loading = true
      const {data} = await this.$post('/opinion/page', {page: false})
      if (data.success) {
        this.opinions = data.data
      } else {
        this.opinions = []
      }
      this.loading = false
    },
    /**
     * 添加当前内容为常用意见
     * @returns {Promise<void>}
     */
    async addOpinion() {
      if (!this.inputValue) {
        this.$message.warning('意见内容不能为空！')
        return
      }
      this.loading = true
      const {data} = await this.$post('/opinion/insert', {opinion: this.inputValue})
      if (data.success) {
        this.$message.success('添加成功')
        await this.loadOpinion()
      } else {
        this.$message.warning(data.message)
      }
      this.loading = false
    },
    async remove(tag) {
      this.$confirm('确定要删除该意见吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        this.loading = true
        const {data} = await this.$post('/opinion/delete', {id: tag.id})
        if (data.success) {
          this.$message.success('删除成功')
          await this.loadOpinion()
        } else {
          this.$message.warning(data.message)
        }
        this.loading = false
      }).catch(() => {
      });
    }
  },
  activated() {
    this.loadOpinion()
  }
}
</script>
<style lang="scss">
.opinion_container {
  .el-tag {
    margin: 0px 4px;
  }

  .el-tag:hover {
    border: solid $--color-info;
  }
}
</style>