package com.dr.process.camunda.command.task.definition;

import com.dr.framework.core.process.bo.TaskDefinition;
import org.camunda.bpm.engine.history.HistoricActivityInstance;
import org.camunda.bpm.engine.impl.bpmn.helper.BpmnProperties;
import org.camunda.bpm.engine.impl.context.Context;
import org.camunda.bpm.engine.impl.interceptor.Command;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;
import org.camunda.bpm.engine.runtime.ActivityInstance;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 根据流程实例Id查询所有的环节的定义
 *
 * @author dr
 */
public class GetProcessTaskDefinitionCmd extends AbstractGetTaskDefinitionCmd implements Command<List<TaskDefinition>> {
    private String processInstanceId;

    public GetProcessTaskDefinitionCmd(String processInstanceId) {
        super(true, true, false);
        this.processInstanceId = processInstanceId;
    }

    @Override
    public List<TaskDefinition> execute(CommandContext commandContext) {
        Assert.isTrue(!StringUtils.isEmpty(processInstanceId), "流程实例id不能为空!");
        String processDefinitionId = null;
        ActivityInstance activityInstance = commandContext.getProcessEngineConfiguration().getRuntimeService().getActivityInstance(processInstanceId);
        if (activityInstance == null) {
            //流转实例查询不到则查询历史数据
            HistoricActivityInstance historicActivityInstance = commandContext.getProcessEngineConfiguration().getHistoryService().createHistoricActivityInstanceQuery().activityId(processInstanceId)
                    .singleResult();
            processDefinitionId = Optional.ofNullable(historicActivityInstance).map(HistoricActivityInstance::getProcessDefinitionId).orElse(null);
        } else {
            processDefinitionId = activityInstance.getProcessDefinitionId();
        }
        Assert.notNull(processDefinitionId, "未查询到指定的流程定义！");
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
