package com.dr.process.camunda.command.task.definition;

import com.dr.framework.core.process.bo.TaskDefinition;
import org.camunda.bpm.engine.impl.bpmn.helper.BpmnProperties;
import org.camunda.bpm.engine.impl.context.Context;
import org.camunda.bpm.engine.impl.interceptor.Command;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 根据流程定义ID查询所有环节定义
 *
 * @author dr
 */
public class GetProcessTaskDefinitionByProcessDefinitionIDCmd extends AbstractGetTaskDefinitionCmd implements Command<List<TaskDefinition>> {
    private String processDefinitionId;

    public GetProcessTaskDefinitionByProcessDefinitionIDCmd(String processDefinitionId) {
        super(true, true, false);
        this.processDefinitionId = processDefinitionId;
    }

    @Override
    public List<TaskDefinition> execute(CommandContext commandContext) {
        Assert.isTrue(!StringUtils.isEmpty(processDefinitionId), "流程定义id不能为空!");
        return Context
                .getProcessEngineConfiguration()
                .getDeploymentCache()
                .findDeployedProcessDefinitionById(processDefinitionId)
                .getActivities()
                .stream()
                .filter(o -> o.getProperties().get(BpmnProperties.TYPE).endsWith("Task"))
                .map(o -> convert(o, commandContext))
                .collect(Collectors.toList());
    }
}
