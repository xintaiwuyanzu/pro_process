package com.dr.process.camunda.service.impl;

import com.dr.framework.core.process.bo.TaskDefinition;
import com.dr.framework.core.process.service.TaskDefinitionService;
import com.dr.process.camunda.command.task.definition.GetNextTaskDefinitionCmd;
import com.dr.process.camunda.command.task.definition.GetPreTaskDefinitionCmd;
import com.dr.process.camunda.command.task.definition.GetProcessTaskDefinitionByProcessDefinitionIDCmd;
import com.dr.process.camunda.command.task.definition.GetProcessTaskDefinitionCmd;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 默认环节定义实现
 *
 * @author dr
 */
@Service
public class DefaultTaskDefinitionServiceImpl extends BaseProcessServiceImpl implements TaskDefinitionService {
    @Override
    public List<TaskDefinition> nextTasks(String taskId, boolean all) {
        return getCommandExecutor().execute(new GetNextTaskDefinitionCmd(taskId, all));
    }

    @Override
    public List<TaskDefinition> preTasks(String taskId, boolean all) {
        return getCommandExecutor().execute(new GetPreTaskDefinitionCmd(taskId, all));
    }

    @Override
    public List<TaskDefinition> processTaskDefinitions(String processInstanceId) {
        return getCommandExecutor().execute(new GetProcessTaskDefinitionCmd(processInstanceId));
    }

    @Override
    public List<TaskDefinition> processTaskDefinitionsByProcessDefinitionId(String processDefinitionID) {
        return getCommandExecutor().execute(new GetProcessTaskDefinitionByProcessDefinitionIDCmd(processDefinitionID));
    }

}
