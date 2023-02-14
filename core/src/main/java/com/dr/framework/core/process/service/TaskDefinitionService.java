package com.dr.framework.core.process.service;

import com.dr.framework.core.process.bo.TaskDefinition;

import java.util.List;

/**
 * 环节定义service
 *
 * @author dr
 */
public interface TaskDefinitionService {
    /**
     * 根据任务实例id查询下一环节定义列表
     *
     * @param taskId
     * @param all    是否包含自定义连接的线
     * @return
     */
    List<TaskDefinition> nextTasks(String taskId, boolean all);

    /**
     * 根据任务实例id查询下一环节定义列表
     *
     * @param taskId
     * @return
     */
    default List<TaskDefinition> nextTasks(String taskId) {
        return nextTasks(taskId, false);
    }

    /**
     * 根据环节实例Id查询该环节前面所有环节定义
     *
     * @param taskId
     * @return
     */
    default List<TaskDefinition> preTasks(String taskId) {
        return preTasks(taskId, false);
    }

    /**
     * 根据环节实例Id查询所有前面的环节
     *
     * @param taskId
     * @param all
     * @return
     */
    List<TaskDefinition> preTasks(String taskId, boolean all);

    /**
     * 根据流程实例查询该流程定义的所有的任务列表
     *
     * @param processInstanceId
     * @return
     */
    List<TaskDefinition> processTaskDefinitions(String processInstanceId);

    /**
     * 根据流程定义id查询所有环节定义
     *
     * @param processDefinitionId
     * @return
     */
    List<TaskDefinition> processTaskDefinitionsByProcessDefinitionId(String processDefinitionId);

}
