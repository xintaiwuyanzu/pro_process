package com.dr.process.camunda.command.process.instance;

import com.dr.framework.core.process.bo.ProcessInstance;
import com.dr.framework.core.process.query.ProcessInstanceQuery;
import org.camunda.bpm.engine.impl.interceptor.Command;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 根据条件查询流转实例
 *
 * @author dr
 */
public class GetProcessInstanceListCmd extends AbstractGetProcessInstanceCmd implements Command<List<ProcessInstance>> {

    public GetProcessInstanceListCmd(ProcessInstanceQuery query) {
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
