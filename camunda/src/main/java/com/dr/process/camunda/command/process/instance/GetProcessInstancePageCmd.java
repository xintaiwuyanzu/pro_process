package com.dr.process.camunda.command.process.instance;

import com.dr.framework.common.page.Page;
import com.dr.framework.core.process.bo.ProcessInstance;
import com.dr.framework.core.process.query.ProcessInstanceQuery;
import org.camunda.bpm.engine.history.HistoricProcessInstanceQuery;
import org.camunda.bpm.engine.impl.interceptor.Command;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;

import java.util.stream.Collectors;

/**
 * 根据条件查询流转实例分页数据
 *
 * @author dr
 */
public class GetProcessInstancePageCmd extends AbstractGetProcessInstanceCmd implements Command<Page<ProcessInstance>> {
    private int start;
    private int end;

    public GetProcessInstancePageCmd(ProcessInstanceQuery query, int start, int end) {
        super(query);
        this.start = start;
        this.end = end;
    }

    @Override
    public Page<ProcessInstance> execute(CommandContext commandContext) {
        HistoricProcessInstanceQuery query = convert(commandContext);
        return new Page(
                start,
                end - start,
                query.count(),
                () ->
                        query.listPage(start, end)
                                .stream()
                                .map(p -> convert(p, commandContext))
                                .collect(Collectors.toList())
        );
    }

}
