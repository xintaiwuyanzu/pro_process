package com.dr.framework.core.process.service;

import com.dr.framework.common.page.Page;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.process.bo.Comment;
import com.dr.framework.core.process.bo.ProcessInstance;
import com.dr.framework.core.process.bo.TaskInstance;
import com.dr.framework.core.process.query.TaskQuery;

import java.util.List;
import java.util.Map;

/**
 * 流程实例service
 *
 * @author dr
 */
public interface TaskInstanceService {
    /**
     * 完成任务，自动跳转下一环节
     *
     * @param taskId
     * @param variables
     */
    void complete(String taskId, Map<String, Object> variables);

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
    List<TaskInstance> taskList(TaskQuery query);

    /**
     * 根据条件查询流程环节分页
     *
     * @param query
     * @param start
     * @param end
     * @return
     */
    Page<TaskInstance> taskPage(TaskQuery query, int start, int end);

    /**
     * 查询任务历史信息
     *
     * @param query
     * @return
     */
    List<TaskInstance> taskHistoryList(TaskQuery query);

    /**
     * 查询流程历史信息分页
     *
     * @param query
     * @param start
     * @param end
     * @return
     */
    Page<TaskInstance> taskHistoryPage(TaskQuery query, int start, int end);


    /**
     * 启动流程
     *
     * @param processId
     * @param variMap
     * @param person
     * @return
     */
    ProcessInstance start(String processId, Map<String, Object> variMap, Person person);

    /**
     * 发送到默认的下一环节，不带有环境变量信息
     *
     * @param taskId
     * @param nextPerson
     */
    default void send(String taskId, String nextPerson) {
        send(taskId, nextPerson, null, null);
    }

    /**
     * 发送到默认的下一环节
     *
     * @param taskId
     * @param nextPerson
     * @param variables
     */
    default void send(String taskId, String nextPerson, Map<String, Object> variables) {
        send(taskId, nextPerson, null, variables);
    }

    /**
     * 发送给下一环节，带有备注
     *
     * @param taskId
     * @param nextPerson
     * @param comment
     */
    default void send(String taskId, String nextPerson, String comment) {
        send(taskId, nextPerson, comment);
    }

    /**
     * 完整的发送方法
     *
     * @param taskId
     * @param nextPerson
     * @param comment
     * @param variables
     */
    void send(String taskId, String nextPerson, String comment, Map<String, Object> variables);


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
    void endProcess(String taskId, String comment);

    /**
     * 跳转
     *
     * @param taskId
     * @param nextTaskId
     * @param nextPerson
     */
    default void jump(String taskId, String nextTaskId, String nextPerson) {
        jump(taskId, nextTaskId, nextPerson, null);
    }

    /**
     * 跳转
     *
     * @param taskId
     * @param nextTaskId
     * @param nextPerson
     * @param comment
     */
    default void jump(String taskId, String nextTaskId, String nextPerson, String comment) {
        jump(taskId, nextTaskId, nextPerson, comment, null);
    }

    /**
     * 跳转到指定的流程环节，可以是往前，也可以是往后
     *
     * @param taskId
     * @param nextTaskId
     * @param nextPerson
     * @param comment
     * @param variables
     */
    void jump(String taskId, String nextTaskId, String nextPerson, String comment, Map<String, Object> variables);

    /**
     * 完成当前环节，并退回到上一环节
     *
     * @param taskId
     * @param comment
     */
    void back(String taskId, String comment);
}
