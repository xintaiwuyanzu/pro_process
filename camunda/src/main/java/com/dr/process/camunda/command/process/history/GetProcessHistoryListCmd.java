package com.dr.process.camunda.command.process.history;

import com.dr.framework.core.process.bo.ProcessInstance;
import com.dr.framework.core.process.query.ProcessInstanceQuery;
import org.camunda.bpm.engine.impl.interceptor.Command;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 根据条件查询流转历史列表
 *
 * @author dr
 */
public class GetProcessHistoryListCmd extends AbstractGetProcessHistoryCmd implements Command<List<ProcessInstance>> {
    public GetProcessHistoryListCmd(ProcessInstanceQuery query) {
        super(query);
    }

    @Override
    public List<ProcessInstance> execute(CommandContext commandContext) {
        return convert(commandContext)
                .list()
                .stream()
                .map(p -> convert(p, commandContext))
                .collect(Collectors.toList());
    }

}
