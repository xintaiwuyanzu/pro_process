package com.dr.process.camunda.command.task.history;

import com.dr.framework.common.page.Page;
import com.dr.framework.core.process.bo.TaskInstance;
import com.dr.framework.core.process.query.TaskQuery;
import org.camunda.bpm.engine.history.HistoricTaskInstanceQuery;
import org.camunda.bpm.engine.impl.interceptor.Command;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;

import java.util.stream.Collectors;

/**
 * 获取任务历史分页
 *
 * @author dr
 */
public class GetTaskHistoryPageCmd extends AbstractGetTaskHistoryCmd implements Command<Page<TaskInstance>> {
    private int start;
    private int end;

    public GetTaskHistoryPageCmd(TaskQuery query, int start, int end) {
        super(query);
        this.start = start;
        this.end = end;
    }

    @Override
    public Page<TaskInstance> execute(CommandContext commandContext) {
        HistoricTaskInstanceQuery hq = convert(commandContext);
        return new Page<>(
                start,
                end - start,
                hq.count(),
                () -> hq.listPage(start, end)
                        .stream()
                        .map(h -> convert(h, commandContext))
                        .collect(Collectors.toList())
        );
    }
}
