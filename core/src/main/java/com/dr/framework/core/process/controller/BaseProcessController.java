package com.dr.framework.core.process.controller;

import com.dr.framework.core.process.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ApplicationObjectSupport;

/**
 * 流程controller基础父类，用来实现通用方法
 *
 * @author dr
 */
public class BaseProcessController extends ApplicationObjectSupport {
    @Autowired
    private ProcessDefinitionService processDefinitionService;
    @Autowired
    private ProcessInstanceService processInstanceService;
    @Autowired
    private TaskDefinitionService taskDefinitionService;
    @Autowired
    private TaskInstanceService taskInstanceService;

    public ProcessDefinitionService getProcessDefinitionService() {
        return processDefinitionService;
    }

    public ProcessInstanceService getProcessInstanceService() {
        return processInstanceService;
    }

    public TaskDefinitionService getTaskDefinitionService() {
        return taskDefinitionService;
    }

    public TaskInstanceService getTaskInstanceService() {
        return taskInstanceService;
    }
}
