package com.dr.process.camunda.service.impl;

import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.organise.service.OrganisePersonService;
import com.dr.framework.core.process.bo.ProcessDefinition;
import com.dr.framework.core.process.service.ProcessContext;
import com.dr.framework.core.process.service.TaskInstanceService;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
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


    public ProcessContext buildContext(ProcessDefinition processDefinition, Person person, Map<String, Object> params) {
        ProcessContext context = new ProcessContext(person, processDefinition);
        context.setBusinessParams(params);
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            context.setRequest((HttpServletRequest) attributes.resolveReference(RequestAttributes.REFERENCE_REQUEST));
        }
        return context;
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
