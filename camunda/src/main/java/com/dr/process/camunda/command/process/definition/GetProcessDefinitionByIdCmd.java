package com.dr.process.camunda.command.process.definition;

import com.dr.framework.core.process.bo.ProcessDefinition;
import com.dr.process.camunda.command.process.AbstractProcessDefinitionCmd;
import org.camunda.bpm.engine.impl.interceptor.Command;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * 根据流程定义id查询流程详细信息
 *
 * @author dr
 */
public class GetProcessDefinitionByIdCmd extends AbstractProcessDefinitionCmd implements Command<ProcessDefinition> {
    private String processDefinitionId;

    public GetProcessDefinitionByIdCmd(String processDefinitionId) {
        super(true, true);
        this.processDefinitionId = processDefinitionId;
    }

    public GetProcessDefinitionByIdCmd(String processDefinitionId, boolean withProperty) {
        super(withProperty);
        this.processDefinitionId = processDefinitionId;
    }

    @Override
    public ProcessDefinition execute(CommandContext commandContext) {
        Assert.isTrue(!StringUtils.isEmpty(processDefinitionId), "流程定义id不能为空！");
        org.camunda.bpm.engine.repository.ProcessDefinition processDefinition = commandContext.getProcessEngineConfiguration().getRepositoryService()
                .getProcessDefinition(processDefinitionId);
        return convertDefinition(processDefinition, commandContext);
    }
}
