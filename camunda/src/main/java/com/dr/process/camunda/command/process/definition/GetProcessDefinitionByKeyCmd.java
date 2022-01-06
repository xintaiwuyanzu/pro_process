package com.dr.process.camunda.command.process.definition;

import com.dr.framework.core.process.bo.ProcessDefinition;
import org.camunda.bpm.engine.impl.interceptor.Command;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * 根据流程定义id查询流程详细信息
 *
 * @author dr
 */
public class GetProcessDefinitionByKeyCmd extends AbstractProcessDefinitionCmd implements Command<ProcessDefinition> {
    private String processDefinitionKey;

    public GetProcessDefinitionByKeyCmd(String processDefinitionKey) {
        super(true, true);
        this.processDefinitionKey = processDefinitionKey;
    }

    public GetProcessDefinitionByKeyCmd(String processDefinitionId, boolean withProperty) {
        super(withProperty);
        this.processDefinitionKey = processDefinitionId;
    }

    @Override
    public ProcessDefinition execute(CommandContext commandContext) {
        Assert.isTrue(!StringUtils.isEmpty(processDefinitionKey), "流程定义编码不能为空！");
        org.camunda.bpm.engine.repository.ProcessDefinition processDefinition = commandContext.getProcessEngineConfiguration()
                .getRepositoryService()
                .createProcessDefinitionQuery()
                .latestVersion()
                .processDefinitionKey(processDefinitionKey)
                .singleResult();
        return convertDefinition(processDefinition, commandContext);
    }
}
