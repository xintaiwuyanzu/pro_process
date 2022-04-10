package com.dr.process.camunda.command.process.definition;

import com.dr.framework.common.page.Page;
import com.dr.framework.core.process.bo.ProcessDefinition;
import com.dr.framework.core.process.query.ProcessDefinitionQuery;
import org.camunda.bpm.engine.impl.interceptor.Command;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;

import java.util.stream.Collectors;

/**
 * 根据条件查询流程定义分页数据
 *
 * @author dr
 */
public class GetProcessDefinitionPageCmd extends AbstractGetProcessQueryCmd implements Command<Page<ProcessDefinition>> {
    private int pageIndex;
    private int pageSize;

    public GetProcessDefinitionPageCmd(ProcessDefinitionQuery processDefinitionQuery, int pageIndex, int pageSize) {
        super(processDefinitionQuery);
        this.pageSize = pageSize;
        this.pageIndex = pageIndex;
    }

    @Override
    public Page<ProcessDefinition> execute(CommandContext commandContext) {
        org.camunda.bpm.engine.repository.ProcessDefinitionQuery processDefinitionQuery = convertQuery(commandContext);
        return new Page(
                pageIndex * pageSize,
                pageSize,
                processDefinitionQuery.count(),
                () -> processDefinitionQuery.listPage(pageIndex * pageSize, pageSize)
                        .stream()
                        .map(p -> convertDefinition(p, commandContext))
                        .collect(Collectors.toList()));
    }
}
