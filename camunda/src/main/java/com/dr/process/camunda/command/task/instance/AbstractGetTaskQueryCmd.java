package com.dr.process.camunda.command.task.instance;

import com.dr.framework.core.process.query.TaskInstanceQuery;
import com.dr.process.camunda.command.QueryUtils;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;

/**
 * @author dr
 */
public class AbstractGetTaskQueryCmd extends AbstractGetTaskInstanceCmd {
    TaskInstanceQuery query;

    public AbstractGetTaskQueryCmd(TaskInstanceQuery query) {
        super(query.isWithVariables(), query.isWithProcessVariables(), query.isWithProperty(), query.isWithProcessProperty(), query.isWithComments());
        this.query = query;
    }

    protected org.camunda.bpm.engine.task.TaskQuery convert(CommandContext commandContext) {
        return QueryUtils.taskInstanceQuery(commandContext, query)
                //根据创建日期倒叙排序
                .orderByTaskCreateTime().desc();
    }


}
