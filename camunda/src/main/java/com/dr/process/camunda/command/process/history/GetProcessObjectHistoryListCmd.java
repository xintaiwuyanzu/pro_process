package com.dr.process.camunda.command.process.history;

import com.dr.framework.core.process.bo.ProcessInstance;
import com.dr.framework.core.process.query.ProcessQuery;
import com.dr.process.camunda.command.process.history.AbstractGetProcessObjectHistoryCmd;
import org.camunda.bpm.engine.impl.interceptor.Command;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author dr
 */
public class GetProcessObjectHistoryListCmd extends AbstractGetProcessObjectHistoryCmd implements Command<List<ProcessInstance>> {
    public GetProcessObjectHistoryListCmd(ProcessQuery query) {
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
