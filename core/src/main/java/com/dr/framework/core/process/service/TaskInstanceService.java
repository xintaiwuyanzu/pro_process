package com.dr.framework.core.process.service;

import com.dr.framework.common.page.Page;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.process.bo.Comment;
import com.dr.framework.core.process.bo.ProcessInstance;
import com.dr.framework.core.process.bo.TaskInstance;
import com.dr.framework.core.process.query.TaskInstanceQuery;

import java.util.List;
import java.util.Map;

/**
 * 流程实例service
 *
 * @author dr
 */
public interface TaskInstanceService {
    /**
     * 设置流程实例环境变量
     *
     * @param taskId
     * @param variables
     */
    void setProcessVariable(String taskId, Map<String, Object> variables);

    /**
     * 设置流程环节环境变量
     *
     * @param taskId
     * @param variables
     */
    void setTaskVariable(String taskId, Map<String, Object> variables);

    /**
     * 获取任务当前环境变量
     *
     * @param taskId
     * @return
     */
    Map<String, Object> getTaskVariables(String taskId);

    /**
     * 获取流程实例环境变量
     *
     * @param processInstanceId
     * @return
     */
    Map<String, Object> getProcessVariables(String processInstanceId);

    /**
     * 删除任务的环境变量
     *
     * @param taskId
     * @param variNames
     */
    void removeTaskVariable(String taskId, String... variNames);

    /**
     * 删除流程实例的环境变量
     *
     * @param processId
     * @param variNames
     */
    void removeProcessVariable(String processId, String... variNames);

    /**
     * 获取环节的批注
     *
     * @param taskId
     * @return
     */
    List<Comment> taskComments(String taskId);

    /**
     * 获取流程实例所有的批注
     *
     * @param processInstanceId
     * @return
     */
    List<Comment> processComments(String processInstanceId);

    /**
     * 添加批注
     *
     * @param taskId
     * @param comments
     */
    void addComment(String taskId, String... comments);

    /**
     * 查询环节详细信息
     *
     * @param taskId
     * @return
     */
    TaskInstance taskInfo(String taskId);

    /**
     * 根据条件查询流程环节信息
     *
     * @param query
     * @return
     */
    List<TaskInstance> taskList(TaskInstanceQuery query);

    /**
     * 根据条件查询流程环节分页
     *
     * @param query
     * @param index
     * @param pageSize
     * @return
     */
    Page<TaskInstance> taskPage(TaskInstanceQuery query, int index, int pageSize);

    /**
     * 查询任务历史信息
     *
     * @param query
     * @return
     */
    List<TaskInstance> taskHistoryList(TaskInstanceQuery query);

    /**
     * 查询流程历史信息分页
     *
     * @param query
     * @param index
     * @param pageSize
     * @return
     */
    Page<TaskInstance> taskHistoryPage(TaskInstanceQuery query, int index, int pageSize);


    /**
     * 启动流程
     *
     * @param processDefinitionId
     * @param variMap
     * @param person
     * @return
     */
    ProcessInstance start(String processDefinitionId, Map<String, Object> variMap, Person person);

    /**
     * 办结任务
     *
     * @param taskId    当前任务实例Id
     * @param variables 任务变量
     * @param person    操作人信息
     */
    void complete(String taskId, Map<String, Object> variables, Person person);

    /**
     * 挂起流程实例
     *
     * @param processInstanceId 流程实例Id
     */
    void suspend(String processInstanceId);

    /**
     * 启动流程实例
     *
     * @param processInstanceId 流程实例Id
     */
    void active(String processInstanceId);

    /**
     * 办结流程，不管当前在哪一环节
     *
     * @param taskId  环节实例id
     * @param comment 批注
     */
    void endProcess(String taskId, Map<String, Object> variables, Person person);
}
