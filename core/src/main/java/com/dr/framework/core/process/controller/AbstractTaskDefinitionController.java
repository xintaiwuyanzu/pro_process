package com.dr.framework.core.process.controller;

import com.dr.framework.common.entity.ResultListEntity;
import com.dr.framework.core.process.bo.TaskDefinition;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 环节定义相关controller
 *
 * @author dr
 */
public class AbstractTaskDefinitionController extends BaseProcessController {

    /**
     * 根据环节实例查询下一环节定义信息
     *
     * @param taskInstanceId 环节实例ID
     * @param all            是否包含所有节点。默认为false。
     *                       如果为true， 除了包含自定义连线，还包含系统自动连线。自动连线是用来随意跳转的
     * @return
     */
    @RequestMapping("nextTasks")
    public ResultListEntity<TaskDefinition> nextTasks(String taskInstanceId, boolean all) {
        return ResultListEntity.success(getTaskDefinitionService().nextTasks(taskInstanceId, all));
    }

    /**
     * 根据环节实例查询当前环节前一环节定义信息
     *
     * @param taskInstanceId
     * @param all
     * @return
     */
    @RequestMapping("preTasks")
    public ResultListEntity<TaskDefinition> preTasks(String taskInstanceId, boolean all) {
        return ResultListEntity.success(getTaskDefinitionService().preTasks(taskInstanceId, all));
    }

    /**
     * 根据流程实例Id查询该流程定义的所有环节定义
     *
     * @param processInstanceId
     * @return
     */
    @RequestMapping("processTasks")
    public ResultListEntity<TaskDefinition> processTasks(String processInstanceId) {
        return ResultListEntity.success(getTaskDefinitionService().processTaskDefinitions(processInstanceId));
    }

    @RequestMapping("processTasksByProDefinitionId")
    public ResultListEntity<TaskDefinition> processTasksByProDefinitionId(String processDefinitionId) {
        return ResultListEntity.success(getTaskDefinitionService().processTaskDefinitionsByProcessDefinitionId(processDefinitionId));
    }


}
