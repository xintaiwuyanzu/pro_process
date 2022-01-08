package com.dr.process.camunda.command.task.history;

import com.dr.framework.core.process.bo.TaskInstance;
import com.dr.framework.core.process.query.TaskInstanceQuery;
import com.dr.process.camunda.command.QueryUtils;
import com.dr.process.camunda.command.TaskInstanceUtils;
import org.camunda.bpm.engine.history.HistoricTaskInstanceQuery;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;
import org.camunda.bpm.engine.impl.persistence.entity.HistoricTaskInstanceEntity;

/**
 * 抽象任务历史父类
 *
 * @author dr
 */
public class AbstractGetTaskHistoryCmd {
    private TaskInstanceQuery query;

    public AbstractGetTaskHistoryCmd(TaskInstanceQuery query) {
        this.query = query;
    }

    protected HistoricTaskInstanceQuery convert(CommandContext commandContext) {
        return QueryUtils.taskHistoryQuery(commandContext, query)
                .orderByHistoricTaskInstanceEndTime()
                .desc();
    }

    /**
     * 转换环节历史实例为对外接口对象
     *
     * @param his
     * @param commandContext
     * @return
     */
    protected TaskInstance convert(HistoricTaskInstanceEntity his, CommandContext commandContext) {
        return TaskInstanceUtils.newTaskInstance(
                his,
                commandContext,
                query.isWithVariables(),
                query.isWithProcessVariables(),
                query.isWithProperty(),
                query.isWithProcessProperty()
        );
    }

}
