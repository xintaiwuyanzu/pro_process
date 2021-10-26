package com.dr.process.camunda.command.process.definition;

import com.dr.framework.core.process.query.ProcessDefinitionQuery;
import org.camunda.bpm.engine.impl.interceptor.Command;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;

/**
 * 根据条件查询流程定义条数
 *
 * @author dr
 */
public class GetProcessDefinitionCountCmd extends AbstractGetProcessQueryCmd implements Command<Long> {

    public GetProcessDefinitionCountCmd(ProcessDefinitionQuery query) {
        super(query);
    }

    @Override
    public Long execute(CommandContext commandContext) {
        return convertQuery(commandContext).count();
    }
}
