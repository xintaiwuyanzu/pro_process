package com.dr.process.camunda.command.task.history;

import com.dr.framework.common.page.Page;
import com.dr.framework.core.process.bo.TaskInstance;
import com.dr.framework.core.process.query.TaskInstanceQuery;
import org.camunda.bpm.engine.history.HistoricTaskInstanceQuery;
import org.camunda.bpm.engine.impl.interceptor.Command;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;
import org.camunda.bpm.engine.impl.persistence.entity.HistoricTaskInstanceEntity;

import java.util.stream.Collectors;

/**
 * 获取任务历史分页
 *
 * @author dr
 */
public class GetTaskHistoryPageCmd extends AbstractGetTaskHistoryCmd implements Command<Page<TaskInstance>> {
    private int index;
    private int pageSize;

    public GetTaskHistoryPageCmd(TaskInstanceQuery query, int start, int end) {
        super(query);
        this.index = start;
        this.pageSize = end;
    }

    @Override
    public Page<TaskInstance> execute(CommandContext commandContext) {
        HistoricTaskInstanceQuery hq = convert(commandContext);
        return new Page<>(
                index * pageSize,
                pageSize,
                hq.count(),
                () -> hq.listPage(index * pageSize, pageSize)
                        .stream()
                        .map(h -> convert((HistoricTaskInstanceEntity) h, commandContext))
                        .collect(Collectors.toList())
        );
    }
}
