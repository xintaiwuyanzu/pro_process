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
     * @return
     */
    List<TaskDefinition> nextTasks(String taskId);

    /**
     * 根据流程实例查询该流程定义的所有的任务列表
     *
     * @param processInstanceId
     * @return
     */
    List<TaskDefinition> processTaskDefinitions(String processInstanceId);

}
