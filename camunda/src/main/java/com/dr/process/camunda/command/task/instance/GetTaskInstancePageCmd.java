package com.dr.process.camunda.command.task.instance;

import com.dr.framework.common.page.Page;
import com.dr.framework.core.process.bo.TaskInstance;
import com.dr.framework.core.process.query.TaskInstanceQuery;
import org.camunda.bpm.engine.impl.interceptor.Command;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;

import java.util.stream.Collectors;

/**
 * 查询环节实例分页
 *
 * @author dr
 */
public class GetTaskInstancePageCmd extends AbstractGetTaskQueryCmd implements Command<Page<TaskInstance>> {
    private int start;
    private int end;

    public GetTaskInstancePageCmd(TaskInstanceQuery query, int start, int end) {
        super(query);
        this.start = start;
        this.end = end;
    }

    @Override
    public Page<TaskInstance> execute(CommandContext commandContext) {
        org.camunda.bpm.engine.task.TaskQuery query = convert(commandContext);
        return new Page<>(
                start,
                end - start,
                query.count(),
                () -> query.list()
                        .stream()
                        .map(t -> convert(t, commandContext))
                        .collect(Collectors.toList())
        );
    }
}
