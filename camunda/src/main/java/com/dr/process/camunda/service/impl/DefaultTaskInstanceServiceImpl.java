package com.dr.process.camunda.service.impl;

import com.dr.framework.common.page.Page;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.process.bo.*;
import com.dr.framework.core.process.query.TaskQuery;
import com.dr.framework.core.process.service.ProcessContext;
import com.dr.framework.core.process.service.ProcessDefinitionService;
import com.dr.framework.core.process.service.ProcessTypeProvider;
import com.dr.framework.core.process.service.TaskInstanceService;
import com.dr.process.camunda.command.process.instance.ConvertProcessInstanceCmd;
import com.dr.process.camunda.command.process.EndProcessCmd;
import com.dr.process.camunda.command.comment.GetProcessCommentsCmd;
import com.dr.process.camunda.command.task.*;
import com.dr.process.camunda.command.comment.GetTaskCommentsCmd;
import com.dr.process.camunda.command.task.history.GetTaskHistoryListCmd;
import com.dr.process.camunda.command.task.history.GetTaskHistoryPageCmd;
import com.dr.process.camunda.command.task.instance.GetTaskInstanceCmd;
import com.dr.process.camunda.command.task.instance.GetTaskInstanceListCmd;
import com.dr.process.camunda.command.task.instance.GetTaskInstancePageCmd;
import org.camunda.bpm.engine.runtime.ProcessInstanceWithVariables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.dr.framework.core.process.service.ProcessConstants.*;

/**
 * 默认环节实例实现
 *
 * @author dr
 */
@Service
public class DefaultTaskInstanceServiceImpl extends BaseProcessServiceImpl implements TaskInstanceService {
    @Autowired
    private ProcessDefinitionService processDefinitionService;
    private Map<String, ProcessTypeProvider> processTypeProviderMap;


    @Override
    public TaskInstance taskInfo(String taskId) {
        return getCommandExecutor().execute(new GetTaskInstanceCmd(taskId));
    }

    @Override
    public List<TaskInstance> taskList(TaskQuery query) {
        return getCommandExecutor().execute(new GetTaskInstanceListCmd(query));
    }

    @Override
    public Page<TaskInstance> taskPage(TaskQuery query, int start, int end) {
        return getCommandExecutor().execute(new GetTaskInstancePageCmd(query, start, end));
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
            getTaskService().createComment(taskObject.getId(), taskObject.getProcessInstanceId(), comment);
        }
    }


    @Override
    @Transactional
    public ProcessInstance start(String processDefinitionId, Map<String, Object> variMap, Person person) {
        ProcessDefinition processDefinition = getProcessDefinitionService().getProcessDefinitionById(processDefinitionId);
        ProcessContext context = buildContext(processDefinition, person, variMap);
        ProcessTypeProvider processTypeProvider = processTypeProviderMap.get(processDefinition.getType());

        Assert.isTrue(processTypeProvider != null, "不支持指定的流程类型：" + processDefinition.getType());
        //启动前拦截
        processTypeProvider.onBeforeStartProcess(context);

        Property proPerty = processDefinition.getProPerty(PROCESS_FORM_URL_KEY);
        if (proPerty != null) {
            context.addVar(PROCESS_FORM_URL_KEY, proPerty.getValue());
        }

        if (StringUtils.hasText(context.getProcessInstanceTitle())) {
            context.addVar(PROCESS_TITLE_KEY, context.getProcessInstanceTitle());
        }
        if (StringUtils.hasText(context.getProcessInstanceDescription())) {
            context.addVar(PROCESS_DESCRIPTION_KEY, context.getProcessInstanceDescription());
        } else {
            context.addVar(PROCESS_DESCRIPTION_KEY, "默认标题！！！");
        }
        //设置启动环节任务人为传进来的登陆人信息
        context.addVar(ASSIGNEE_KEY, person.getId());
        //设置流程任务类型变量
        context.addVar(PROCESS_TYPE_KEY, processTypeProvider.getType());
        //调用流程引擎启动流程
        ProcessInstanceWithVariables instance = (ProcessInstanceWithVariables) getRuntimeService().startProcessInstanceById(processDefinition.getId(), context.getBusinessId(), context.getProcessVarMap());
        //转换流程实例对象
        ProcessInstance instance1 = getCommandExecutor().execute(new ConvertProcessInstanceCmd(instance.getId(), instance.getVariables()));

        context.setProcessInstance(instance1);
        //启动后拦截
        processTypeProvider.onAfterStartProcess(context);
        //有可能启动后直接跳转到第二个环节
        if (variMap.containsKey(VAR_NEXT_TASK_ID) && variMap.containsKey(VAR_NEXT_TASK_PERSON)) {
            String taskId = (String) variMap.get(VAR_NEXT_TASK_ID);
            String personId = (String) variMap.get(VAR_NEXT_TASK_PERSON);
            if (StringUtils.hasText(taskId) && StringUtils.hasText(personId)) {
                send(taskId, personId, (String) variMap.get(VAR_COMMENT_KEY), variMap);
            }
        }
        return instance1;
    }


    @Override
    public void complete(String taskId, Map<String, Object> variables) {
        Assert.isTrue(!StringUtils.isEmpty(taskId), "环节Id不能为空！");
        getTaskService().complete(taskId, variables);
    }

    /**
     * 发送到下一环节
     *
     * @param taskId
     * @param nextPerson
     * @param comment
     * @param variables
     */
    @Override
    @Transactional
    public void send(String taskId, String nextPerson, String comment, Map<String, Object> variables) {
        getCommandExecutor().execute(new SendTaskCmd(taskId, nextPerson, comment, variables));
    }

    @Override
    public void suspend(String processInstanceId) {
        Assert.isTrue(!StringUtils.isEmpty(processInstanceId), "流程实例不能为空");
        getRuntimeService().suspendProcessInstanceById(processInstanceId);
    }

    @Override
    public void active(String processInstanceId) {
        Assert.isTrue(!StringUtils.isEmpty(processInstanceId), "流程实例不能为空");
        getRuntimeService().activateProcessInstanceById(processInstanceId);
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
    public void afterPropertiesSet() {
        processTypeProviderMap = getApplicationContext().getBeansOfType(ProcessTypeProvider.class).values().stream().collect(Collectors.toMap(ProcessTypeProvider::getType, t -> t));
    }

    public ProcessDefinitionService getProcessDefinitionService() {
        return processDefinitionService;
    }
}
