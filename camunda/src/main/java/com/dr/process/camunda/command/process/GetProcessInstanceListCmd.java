package com.dr.process.camunda.command.process;

import com.dr.framework.core.process.bo.ProcessInstance;
import com.dr.framework.core.process.query.ProcessQuery;
import org.camunda.bpm.engine.impl.interceptor.Command;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author dr
 */
public class GetProcessInstanceListCmd extends AbstractGetProcessInstanceCmd implements Command<List<ProcessInstance>> {

    public GetProcessInstanceListCmd(ProcessQuery query) {
        super(query);
    }

    @Override
    public List<ProcessInstance> execute(CommandContext commandContext) {
        return convert(commandContext)
                .list()
                .stream()
                .map(p -> convert(p, commandContext)
                ).collect(Collectors.toList());
    }


}
