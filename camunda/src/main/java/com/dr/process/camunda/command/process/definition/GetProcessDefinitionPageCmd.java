package com.dr.process.camunda.command.process.definition;

import com.dr.framework.common.page.Page;
import com.dr.framework.core.process.bo.ProcessDefinition;
import com.dr.framework.core.process.query.ProcessDefinitionQuery;
import org.camunda.bpm.engine.impl.interceptor.Command;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;

import java.util.stream.Collectors;

/**
 * @author dr
 */
public class GetProcessDefinitionPageCmd extends AbstractGetProcessQueryCmd implements Command<Page<ProcessDefinition>> {
    private int start;
    private int end;

    public GetProcessDefinitionPageCmd(ProcessDefinitionQuery processDefinitionQuery, int start, int end) {
        super(processDefinitionQuery);
        this.end = end;
        this.start = start;
    }

    @Override
    public Page<ProcessDefinition> execute(CommandContext commandContext) {
        org.camunda.bpm.engine.repository.ProcessDefinitionQuery processDefinitionQuery = convertQuery(commandContext);
        return new Page(start,
                end - start,
                processDefinitionQuery.count(),
                () -> processDefinitionQuery.listPage(start, end)
                        .stream()
                        .map(p -> convertDefinition(p, commandContext))
                        .collect(Collectors.toList()));
    }
}
