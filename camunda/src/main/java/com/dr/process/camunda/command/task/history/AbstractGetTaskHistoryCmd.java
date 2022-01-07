package com.dr.process.camunda.command.task.history;

import com.dr.framework.core.process.bo.TaskInstance;
import com.dr.framework.core.process.query.TaskInstanceQuery;
import com.dr.process.camunda.command.TaskInstanceUtils;
import org.camunda.bpm.engine.history.HistoricTaskInstanceQuery;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;
import org.camunda.bpm.engine.impl.persistence.entity.HistoricTaskInstanceEntity;
import org.springframework.util.StringUtils;

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
        HistoricTaskInstanceQuery hq = commandContext.getProcessEngineConfiguration()
                .getHistoryService()
                .createHistoricTaskInstanceQuery();
        if (query != null) {
            if (!StringUtils.isEmpty(query.getOwner())) {
                hq.taskOwner(query.getOwner());
            }
            if (!StringUtils.isEmpty(query.getAssignee())) {
                hq.taskAssignee(query.getAssignee());
            }
            if (!StringUtils.isEmpty(query.getTaskKeyLike())) {
                hq.taskDefinitionKeyIn(query.getTaskKeyLike());
            }
            if (!StringUtils.isEmpty(query.getTitle())) {
                hq.taskNameLike(query.getTitle());
            }
            if (!StringUtils.isEmpty(query.getDescription())) {
                hq.taskDescriptionLike(query.getDescription());
            }
            if (!StringUtils.isEmpty(query.getProcessInstanceId())) {
                hq.processInstanceId(query.getProcessInstanceId());
            }
        }
        hq.orderByHistoricActivityInstanceStartTime()
                .asc();
        return hq;
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
