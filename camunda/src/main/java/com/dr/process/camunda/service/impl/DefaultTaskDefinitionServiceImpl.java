package com.dr.process.camunda.service.impl;

import com.dr.framework.core.process.bo.TaskDefinition;
import com.dr.framework.core.process.service.TaskDefinitionService;
import com.dr.process.camunda.command.task.GetNextTaskDefinitionCmd;
import com.dr.process.camunda.command.task.GetProcessTaskDefinitionCmd;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;

import java.util.List;

/**
 * 默认环节定义实现
 *
 * @author dr
 */
public class DefaultTaskDefinitionServiceImpl extends BaseProcessServiceImpl implements TaskDefinitionService {

    public DefaultTaskDefinitionServiceImpl(ProcessEngineConfigurationImpl processEngineConfiguration) {
        super(processEngineConfiguration);
    }

    @Override
    public List<TaskDefinition> nextTasks(String taskId) {
        return getCommandExecutor().execute(new GetNextTaskDefinitionCmd(taskId));
    }

    @Override
    public List<TaskDefinition> processTaskDefinitions(String processInstanceId) {
        return getCommandExecutor().execute(new GetProcessTaskDefinitionCmd(processInstanceId));
    }

}
