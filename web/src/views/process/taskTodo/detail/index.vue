<template>
  <section>
    <nac-info title="任务详情" back>
      <el-button type="primary" v-show="()=>properties.btnSee" @click="sendNext($route.query.taskId)">
        发送
      </el-button>
      <el-button type="primary">
        回退
      </el-button>
      <el-button type="primary">
        办结
      </el-button>
      <el-button type="primary">
        填写意见
      </el-button>
      <el-button type="primary" @click="showHistory($route.query.processInstanceId)">流转历史</el-button>
    </nac-info>
    <section class="index_main">
      <task-container :task-instance-id="taskInstanceId"/>
    </section>
  </section>
</template>
<script>
import abstractProcess from "../../../../lib/abstractProcess";
import TaskContainer from "../../../../lib/taskContainer";

/**
 * 任务详情页面
 */
export default {
  components: {TaskContainer},
  extends: abstractProcess,
  data() {
    return {taskInstanceId: ''}
  },
  mounted() {
    this.taskInstanceId = this.$route.query.taskId
  }
}
</script>