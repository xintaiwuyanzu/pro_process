package com.dr.process.camunda.service.impl;

import com.dr.framework.common.page.Page;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.organise.service.OrganisePersonService;
import com.dr.framework.core.process.bo.*;
import com.dr.framework.core.process.query.TaskQuery;
import com.dr.framework.core.process.service.ProcessDefinitionService;
import com.dr.framework.core.process.service.TaskInstanceService;
import com.dr.process.camunda.command.process.ConvertProcessInstanceCmd;
import com.dr.process.camunda.command.process.EndProcessCmd;
import com.dr.process.camunda.command.process.GetProcessCommentsCmd;
import com.dr.process.camunda.command.task.*;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.runtime.ProcessInstanceWithVariables;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.dr.framework.core.process.service.ProcessService.FORM_URL_KEY;

/**
 * 默认环节实例实现
 *
 * @author dr
 */
public class DefaultTaskInstanceServiceImpl extends BaseProcessServiceImpl implements TaskInstanceService {
    private TaskService taskService;
    private OrganisePersonService organisePersonService;
    private RuntimeService runtimeService;
    private ProcessDefinitionService processDefinitionService;

    public DefaultTaskInstanceServiceImpl(ProcessEngineConfigurationImpl processEngineConfiguration, ProcessDefinitionService processDefinitionService) {
        super(processEngineConfiguration);
        this.processDefinitionService = processDefinitionService;
    }

    @Override
    public TaskInstance taskInfo(String taskId) {
        return getCommandExecutor().execute(new GetTaskCmd(taskId));
    }

    @Override
    public List<TaskInstance> taskList(TaskQuery query) {
        return getCommandExecutor().execute(new GetTaskListCmd(query));
    }

    @Override
    public Page<TaskInstance> taskPage(TaskQuery query, int start, int end) {
        return getCommandExecutor().execute(new GetTaskPageCmd(query, start, end));
    }

    @Override
    public List<TaskInstance> taskHistoryList(TaskQuery query) {
        return getCommandExecutor().execute(new GetTaskHistoryListCmd(query));
    }

    @Override
    public Page<TaskInstance> taskHistoryPage(TaskQuery query, int start, int end) {
        return getCommandExecutor().execute(new GetTaskHistoryPageCmd(query, start, end));
    }

    @Override
    public Map<String, Object> getProcessVariables(String taskId) {
        return getTaskService().getVariables(taskId);
    }

    @Override
    public void setProcessVariable(String taskId, Map<String, Object> variables) {
        getTaskService().setVariables(taskId, variables);
    }

    @Override
    public void removeProcessVariable(String taskId, String... variNames) {
        List<String> strings = Arrays.asList(variNames);
        getTaskService().removeVariables(taskId, strings);
    }

    @Override
    public Map<String, Object> getTaskVariables(String taskId) {
        return getTaskService().getVariablesLocal(taskId);
    }

    @Override
    public void setTaskVariable(String taskId, Map<String, Object> variables) {
        getTaskService().setVariablesLocal(taskId, variables);
    }

    @Override
    public void removeTaskVariable(String taskId, String... variNames) {
        List<String> strings = Arrays.asList(variNames);
        getTaskService().removeVariablesLocal(taskId, strings);
    }

    @Override
    public List<Comment> taskComments(String taskId) {
        return getCommandExecutor().execute(new GetTaskCommentsCmd(taskId, getOrganisePersonService()));
    }

    @Override
    public List<Comment> processComments(String processInstanceId) {
        return getCommandExecutor().execute(new GetProcessCommentsCmd(processInstanceId, getOrganisePersonService()));
    }

    @Override
    public void addComment(String taskId, String... comments) {
        TaskInstance taskObject = taskInfo(taskId);
        for (String comment : comments) {
            taskService.createComment(taskObject.getId(), taskObject.getProcessInstanceId(), comment);
        }
    }


    @Override
    @Transactional
    public ProcessInstance start(String processId, Map<String, Object> variMap, Person person) {
        ProcessDefinition processDefinition = getProcessDefinitionService().getProcessDefinitionById(processId);
        ProPerty proPerty = processDefinition.getProPerty(FORM_URL_KEY);
        if (proPerty != null) {
            variMap.put(FORM_URL_KEY, proPerty.getValue());
        }
        ProcessInstanceWithVariables instance = (ProcessInstanceWithVariables) runtimeService.startProcessInstanceById(processDefinition.getId(), variMap);
        return getCommandExecutor().execute(
                new ConvertProcessInstanceCmd(
                        instance.getId(),
                        instance.getVariables()
                )
        );
    }


    @Override
    public void complete(String taskId, Map<String, Object> variables) {
        Assert.isTrue(!StringUtils.isEmpty(taskId), "环节Id不能为空！");
        taskService.complete(taskId, variables);
    }


    @Override
    public void send(String taskId, String nextPerson, String comment, Map<String, Object> variables) {
        getCommandExecutor().execute(new SendTaskCmd(taskId, nextPerson, comment, variables));
    }

    @Override
    public void suspend(String processInstanceId) {
        Assert.isTrue(!StringUtils.isEmpty(processInstanceId), "流程实例不能为空");
        runtimeService.suspendProcessInstanceById(processInstanceId);
    }

    @Override
    public void active(String processInstanceId) {
        Assert.isTrue(!StringUtils.isEmpty(processInstanceId), "流程实例不能为空");
        runtimeService.activateProcessInstanceById(processInstanceId);
    }

    @Override
    public void endProcess(String taskId, String comment) {
        getCommandExecutor().execute(new EndProcessCmd(taskId, comment));
    }

    @Override
    public void back(String taskId, String comment) {
        getCommandExecutor().execute(new BackTaskCmd(taskId, comment));
    }

    @Override
    public void jump(String taskId, String nextTaskId, String nextPerson, String comment, Map<String, Object> variables) {
        getCommandExecutor().execute(new JumpTaskCmd(taskId, nextTaskId, nextPerson, comment, variables));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        taskService = getApplicationContext().getBean(TaskService.class);
        organisePersonService = getApplicationContext().getBean(OrganisePersonService.class);
        runtimeService = getApplicationContext().getBean(RuntimeService.class);
    }

    public TaskService getTaskService() {
        return taskService;
    }

    public OrganisePersonService getOrganisePersonService() {
        return organisePersonService;
    }

    public RuntimeService getRuntimeService() {
        return runtimeService;
    }

    public ProcessDefinitionService getProcessDefinitionService() {
        return processDefinitionService;
    }

}
