<template>
  <div v-show="openHistoryDialog" class="taskTodoDialog">
    <div style="display: flex;justify-content: space-between; height:50px;border-bottom:1px solid #000000;">
      <h5 style="height: 50px; line-height: 50px; margin-left:35px;">流转历史</h5>
      <div>
        <el-button type="primary" @click.stop="openHistoryDialog=false">关闭</el-button>
      </div>
    </div>
    <ul class="taskTodoDialogHeader">
      <li style="margin-left:55px;">
        <button style="background: #5B9BD5"/>
        <span class="headItemName">已办环节</span>
      </li>
      <li style="margin-left:15px;">
        <button style="background: #FFC000"></button>
        <span class="headItemName">当前环节</span>
      </li>
      <li style="margin-left:15px;">
        <button style="background:#FFFFFF"></button>
        <span class="headItemName">未办环节</span>
      </li>
    </ul>
    <ul class="taskTodoDialogContent">
      <li style="margin-left:45px;">
        <button class="circleButton">开始</button>
      </li>
      <li v-for="item in taskInstanceList" :key="item.id"
          style="height:60px; align-items: center;">
        <span><i class="arrowPre"/><i class="arrowSuffix"/></span>
        <button
            v-if="taskList.map(item => item.name).indexOf(item.name)===0&&processParams.name ===(item.name) "
            style="background: #FFC000;">
          {{ item.name }}
        </button>
        <button v-else-if="taskList.map(item => item.name).indexOf(item.name)===-1" style="background: #fff;">
          {{ item.name }}
        </button>
        <button v-else style="background: #5B9BD5;"> {{ item.name }}</button>
      </li>
      <li>
        <span><i class="arrowPre"/><i class="arrowSuffix"/></span>
        <button class="circleButton">结束</button>
      </li>
    </ul>
    <div class="taskTodoDialogTable">
      <table-render :columns="columns" :data="taskList" style="height: 300px;" :index="true"/>
    </div>
  </div>
</template>

<script>
import {http} from "@dr/framework/src/plugins/http";

/**
 * 流转历史弹窗
 */
export default {
  name: "index",
  props: {
    processParams: {type: Object},//当前环节
  },
  data() {
    return {
      openHistoryDialog: false,
      taskList: [],
      columns: {
        ownerName: {label: '任务发起人', search: true, width: 100},
        assigneeName: {label: '发送人', width: 100},
        createDate: {dateFormat: true, label: '发送时间', width: 100},
        name: {label: '环节定义名称', width: 160},
        message: {label: '审核意见'}
      },
      taskInstanceList: []
    }
  },
  methods: {
    async openHistory() {
      this.openHistoryDialog = true
    },
    //查询环节历史
    async getTaskInstances(processParams) {
      const {data} = await http().post('/processTaskInstance/TaskInstanceHistoryPage', {
        page: false,
        // withVariables:true,
        // withProcessVariables:true,
        processInstanceId: processParams.processInstanceId
      })
      if (data.success) {
        this.taskList = []
        data.data.forEach(item => {
          this.taskList.push({
            ownerName: item.ownerName,
            assigneeName: item.assigneeName,
            createDate: item.createDate,
            name: item.name,
            message: item.comment && item.comment.message ? item.comment.message : ''
          })
        })
      } else {
        this.$message.error('查询历史失败，请稍后重试')
      }
    },
    //根据流程定义id查询环节定义
    async getDefinitionTasks(processParams) {
      const {data} = await this.$http.post('taskDefinition/processTasksByProDefinitionId', {processDefinitionId: processParams.processDefineId})
      //TODO 要根据nextIds 下一节点,preIds 上一节点 排序 暂时没有好的方法 后续优化
      this.taskInstanceList = []
      data.data.forEach(i => {
        i.preIds.forEach(id => {
          var preNode = this.getNodes(data.data, id)
          //没有上一节点 说明是第一个节点
          if (preNode == null) {
            this.taskInstanceList.push(i)
            //根据这个节点找后面的节点
            this.addNextNodes(data.data, i)
          }
        })
      })
      //this.taskInstanceList = data.data
      this.$emit('doLoading', false)
    },
    //取下一节点
    addNextNodes(arr, i) {
      i.nextIds.forEach(id => {
        var nextNode = this.getNodes(arr, id)
        if (nextNode != null) {
          this.taskInstanceList.push(nextNode)
          this.addNextNodes(arr, nextNode)
        }
      })
    },
    //取节点
    getNodes(arr, i) {
      var node = null
      arr.forEach(j => {
        if (j.id === i) {
          node = j
        }
      })
      return node
    },
    //根据流程实例id查询当前环节实例
    async getCurrentTaskInstances(row) {
      const {data} = await this.$http.post('processTaskInstance/taskInstancePage', {
        page: false,
        processInstanceId: row.processInstanceId
      })
      if (data.data.length > 0) {
        //TODO 直接修改prop值了，不建议使用
        this.processParams.name = data.data[0].name
      }
    },
  },
  watch: {
    async processParams(v) {
      this.$emit('doLoading', true)
      await this.getTaskInstances(v)
      //TODO 待办任务可以不用再查询了
      await this.getCurrentTaskInstances(v)
      await this.getDefinitionTasks(v)
    }
  }
}
</script>

<style lang="scss" scoped>
.taskTodoDialog {
  width: 880px;
  height: 550px;
  position: absolute;
  left: 50%;
  top: 50%;
  z-index: 100;
  //background: $--color-primary;
  background: #BDD7EE;
  transform: translate(-50%, -50%);

  .taskTodoDialogHeader {
    width: 100%;
    display: flex;
    justify-content: flex-start;
    height: 60px;
    align-items: center;

    li {
      list-style-type: none;

      button {
        outline: none;
        border-radius: 3px;
        width: 30px;
        height: 12px;
        border: none;
      }

      .headItemName {
        margin-left: 5px;
      }
    }
  }

  .taskTodoDialogContent {
    width: 100%;
    display: flex;
    justify-content: flex-start;

    li {
      list-style-type: none;
      display: flex;
      justify-content: flex-start;

      .circleButton {
        width: 60px;
        height: 60px;
        line-height: 60px;
        text-align: center;
        border-radius: 50%;
        background: #fff;
      }

      span {
        position: relative;
        height: 60px;
        width: 80px;

        .arrowPre {
          position: absolute;
          width: 70px;
          height: 1px;
          background: #000000;
          top: 50%;
          transform: translateY(-50%);
          left: 0;
        }

        .arrowSuffix {
          position: absolute;
          width: 0;
          height: 0;
          border-top: 5px solid transparent;
          border-left: 10px solid black;
          border-bottom: 5px solid transparent;
          top: 50%;
          transform: translateY(-50%);
          right: 0;
        }
      }

      button {
        width: 120px;
        height: 60px;
        line-height: 60px;
        text-align: center;
        border-radius: 5px;
      }
    }
  }

  .taskTodoDialogTable {
    width: 100%;
    display: flex;
    margin-top: 40px;
    justify-content: center;
    align-items: center
  }
}
</style>