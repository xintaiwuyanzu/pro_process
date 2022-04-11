package com.dr.process.camunda.service.impl;

import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.organise.service.OrganisePersonService;
import com.dr.framework.core.process.bo.ProcessDefinition;
import com.dr.framework.core.process.bo.TaskInstance;
import com.dr.framework.core.process.service.ProcessConstants;
import com.dr.framework.core.process.service.ProcessContext;
import com.dr.framework.core.process.service.ProcessDefinitionService;
import com.dr.framework.core.process.service.TaskContext;
import com.dr.process.camunda.command.task.instance.GetTaskInstanceCmd;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.interceptor.CommandExecutor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 流程定义抽象父类
 *
 * @author dr
 */
public abstract class BaseProcessServiceImpl implements InitializingBean, ApplicationContextAware {
    @Autowired
    protected OrganisePersonService organisePersonService;
    @Autowired
    protected TaskService taskService;
    @Autowired
    protected RuntimeService runtimeService;
    @Autowired
    protected ProcessEngineConfigurationImpl processEngineConfiguration;
    @Autowired
    protected CommandExecutor commandExecutor;
    private ApplicationContext applicationContext;
    @Autowired
    private ProcessDefinitionService processDefinitionService;

    /**
     * 构造流程上下文
     *
     * @param processDefinition
     * @param person
     * @param params
     * @return
     */
    public ProcessContext buildTaskContext(ProcessDefinition processDefinition, Person person, Map<String, Object> params) {
        ProcessContext context = new ProcessContext(person, processDefinition);
        context.setBusinessParams(params);

        bindDefaultVars(context, params);
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            context.setRequest((HttpServletRequest) attributes.resolveReference(RequestAttributes.REFERENCE_REQUEST));
        }
        return context;
    }

    void bindDefaultVars(ProcessContext context, Map<String, Object> params) {
        if (params.containsKey(ProcessConstants.VAR_NEXT_TASK_PERSON)) {
            context.addVar(ProcessConstants.VAR_NEXT_TASK_PERSON, params.get(ProcessConstants.VAR_NEXT_TASK_PERSON));
        }
        if (params.containsKey(ProcessConstants.VAR_COMMENT_KEY)) {
            context.addVar(ProcessConstants.VAR_COMMENT_KEY, params.get(ProcessConstants.VAR_COMMENT_KEY));
        }
        if (params.containsKey(ProcessConstants.VAR_NEXT_TASK_ID)) {
            context.addVar(ProcessConstants.VAR_NEXT_TASK_ID, params.get(ProcessConstants.VAR_NEXT_TASK_ID));
        }
        if (params.containsKey(ProcessConstants.TASK_ASSIGNEE_KEY)) {
            context.addVar(ProcessConstants.TASK_ASSIGNEE_KEY, params.get(ProcessConstants.TASK_ASSIGNEE_KEY));
        }
    }

    /**
     * 构造环节上下文
     *
     * @param taskId
     * @param variables
     * @param person
     * @return
     */
    protected TaskContext buildTaskContext(String taskId, Map<String, Object> variables, Person person) {
        //查询环节实例
        TaskInstance taskInstance = taskInfo(taskId);
        //查询流程实例
        ProcessDefinition processDefinition = getProcessDefinitionService().getProcessDefinitionById(taskInstance.getProcessDefineId());

        TaskContext context = new TaskContext(person, processDefinition, taskInstance);
        context.setBusinessParams(variables);

        bindDefaultVars(context, variables);

        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            context.setRequest((HttpServletRequest) attributes.resolveReference(RequestAttributes.REFERENCE_REQUEST));
        }
        return context;
    }

    public ProcessDefinitionService getProcessDefinitionService() {
        return processDefinitionService;
    }

    public TaskInstance taskInfo(String taskId) {
        return getCommandExecutor().execute(new GetTaskInstanceCmd(taskId));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public OrganisePersonService getOrganisePersonService() {
        return organisePersonService;
    }

    public TaskService getTaskService() {
        return taskService;
    }

    public ProcessEngineConfigurationImpl getProcessEngineConfiguration() {
        return processEngineConfiguration;
    }

    public RuntimeService getRuntimeService() {
        return runtimeService;
    }

    public CommandExecutor getCommandExecutor() {
        return commandExecutor;
    }
}
