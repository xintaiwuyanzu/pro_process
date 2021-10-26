package com.dr.framework.core.process.service.impl;

import com.dr.framework.core.process.service.ProcessDefinitionService;
import com.dr.framework.core.process.service.ProcessInstanceService;
import com.dr.framework.core.process.service.TaskDefinitionService;
import com.dr.framework.core.process.service.TaskInstanceService;

/**
 * 所有流程相关service汇总
 *
 * @author dr
 */
public interface ProcessServices {

    ProcessDefinitionService getProcessDefinitionService();

    ProcessInstanceService getProcessInstanceService();

    TaskDefinitionService getTaskDefinitionService();

    TaskInstanceService getTaskInstanceService();
}
