package com.dr.framework.core.process.service.impl;

import com.dr.framework.common.page.Page;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.process.bo.*;
import com.dr.framework.core.process.query.ProcessDefinitionQuery;
import com.dr.framework.core.process.query.ProcessQuery;
import com.dr.framework.core.process.query.TaskQuery;
import com.dr.framework.core.process.service.ProcessService;

import java.util.List;
import java.util.Map;

/**
 * 流程汇总service抽象父类
 *
 * @author dr
 */
public interface ProcessServiceComposite extends ProcessService, ProcessServices {

    @Override
    default ProcessDefinition getProcessDefinitionById(String id) {
        return getProcessDefinitionService().getProcessDefinitionById(id);
    }

    @Override
    default ProcessDefinition getProcessDefinitionByKey(String key) {
        return getProcessDefinitionService().getProcessDefinitionByKey(key);
    }

    @Override
    default long countDefinition(ProcessDefinitionQuery query) {
        return getProcessDefinitionService().countDefinition(query);
    }

    @Override
    default List<ProcessDefinition> processDefinitionList(ProcessDefinitionQuery processDefinitionQuery) {
        return getProcessDefinitionService().processDefinitionList(processDefinitionQuery);
    }

    @Override
    default Page<ProcessDefinition> processDefinitionPage(ProcessDefinitionQuery processDefinitionQuery, int start, int end) {
        return getProcessDefinitionService().processDefinitionPage(processDefinitionQuery, start, end);
    }

    @Override
    default List<ProcessInstance> processInstanceList(ProcessQuery query) {
        return getProcessInstanceService().processInstanceList(query);
    }

    @Override
    default Page<ProcessInstance> processInstancePage(ProcessQuery query, int start, int end) {
        return getProcessInstanceService().processInstancePage(query, start, end);
    }

    @Override
    default List<ProcessInstance> processInstanceHistoryList(ProcessQuery query) {
        return getProcessInstanceService().processInstanceHistoryList(query);
    }

    @Override
    default Page<ProcessInstance> processInstanceHistoryPage(ProcessQuery query, int start, int end) {
        return getProcessInstanceService().processInstanceHistoryPage(query, start, end);
    }

    @Override
    default void deleteProcessInstance(String processInstance, String deleteReason) {
        getProcessInstanceService().deleteProcessInstance(processInstance, deleteReason);
    }

    @Override
    default List<TaskDefinition> nextTasks(String taskId) {
        return getTaskDefinitionService().nextTasks(taskId);
    }

    @Override
    default List<TaskDefinition> processTaskDefinitions(String processInstanceId) {
        return getTaskDefinitionService().processTaskDefinitions(processInstanceId);
    }

    @Override
    default void complete(String taskId, Map<String, Object> variables) {
        getTaskInstanceService().complete(taskId, variables);
    }

    @Override
    default void setProcessVariable(String taskId, Map<String, Object> variables) {
        getTaskInstanceService().setProcessVariable(taskId, variables);
    }

    @Override
    default void setTaskVariable(String taskId, Map<String, Object> variables) {
        getTaskInstanceService().setTaskVariable(taskId, variables);
    }

    @Override
    default Map<String, Object> getTaskVariables(String taskId) {
        return getTaskInstanceService().getTaskVariables(taskId);
    }

    @Override
    default Map<String, Object> getProcessVariables(String processInstanceId) {
        return getTaskInstanceService().getProcessVariables(processInstanceId);
    }

    @Override
    default void removeTaskVariable(String taskId, String... variNames) {
        getTaskInstanceService().removeTaskVariable(taskId, variNames);
    }

    @Override
    default void removeProcessVariable(String processId, String... variNames) {
        getTaskInstanceService().removeProcessVariable(processId, variNames);
    }

    @Override
    default List<Comment> taskComments(String taskId) {
        return getTaskInstanceService().taskComments(taskId);
    }

    @Override
    default List<Comment> processComments(String processInstanceId) {
        return getTaskInstanceService().processComments(processInstanceId);
    }

    @Override
    default void addComment(String taskId, String... comments) {
        getTaskInstanceService().addComment(taskId, comments);
    }

    @Override
    default TaskInstance taskInfo(String taskId) {
        return getTaskInstanceService().taskInfo(taskId);
    }

    @Override
    default List<TaskInstance> taskList(TaskQuery query) {
        return getTaskInstanceService().taskList(query);
    }

    @Override
    default Page<TaskInstance> taskPage(TaskQuery query, int start, int end) {
        return getTaskInstanceService().taskPage(query, start, end);
    }

    @Override
    default List<TaskInstance> taskHistoryList(TaskQuery query) {
        return getTaskInstanceService().taskHistoryList(query);
    }

    @Override
    default Page<TaskInstance> taskHistoryPage(TaskQuery query, int start, int end) {
        return getTaskInstanceService().taskHistoryPage(query, start, end);
    }

    @Override
    default ProcessInstance start(String processId, Map<String, Object> variMap, Person person) {
        return getTaskInstanceService().start(processId, variMap, person);
    }

    @Override
    default void send(String taskId, String nextPerson, String comment, Map<String, Object> variables) {
        getTaskInstanceService().send(taskId, nextPerson, comment, variables);
    }

    @Override
    default void suspend(String processInstanceId) {
        getTaskInstanceService().suspend(processInstanceId);
    }

    @Override
    default void active(String processInstanceId) {
        getTaskInstanceService().active(processInstanceId);
    }

    @Override
    default void endProcess(String taskId, String comment) {
        getTaskInstanceService().endProcess(taskId, comment);
    }

    @Override
    default void jump(String taskId, String nextTaskId, String nextPerson, String comment, Map<String, Object> variables) {
        getTaskInstanceService().jump(taskId, nextTaskId, nextPerson, comment, variables);
    }

    @Override
    default void back(String taskId, String comment) {
        getTaskInstanceService().back(taskId, comment);
    }

}
