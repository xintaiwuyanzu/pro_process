package com.dr.process.camunda.command.process.definition;

import com.dr.framework.core.process.bo.ProcessDefinition;
import com.dr.framework.core.process.query.ProcessDefinitionQuery;
import org.camunda.bpm.engine.impl.interceptor.Command;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 根据条件查询流程定义列表
 *
 * @author dr
 */
public class GetProcessDefinitionListCmd extends AbstractGetProcessQueryCmd implements Command<List<ProcessDefinition>> {
    public GetProcessDefinitionListCmd(ProcessDefinitionQuery query) {
        super(query);
    }

    @Override
    public List<ProcessDefinition> execute(CommandContext commandContext) {
        return convertQuery(commandContext)
                .list()
                .stream()
                .map(p -> convertDefinition(p, commandContext))
                .collect(Collectors.toList());
    }
}
