package com.dr.process.camunda.command.task.instance;

import com.dr.framework.common.page.Page;
import com.dr.framework.core.process.bo.TaskInstance;
import com.dr.framework.core.process.query.TaskInstanceQuery;
import org.camunda.bpm.engine.impl.interceptor.Command;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;
import org.camunda.bpm.engine.impl.persistence.entity.TaskEntity;

import java.util.stream.Collectors;

/**
 * 查询环节实例分页
 *
 * @author dr
 */
public class GetTaskInstancePageCmd extends AbstractGetTaskQueryCmd implements Command<Page<TaskInstance>> {
    private int index;
    private int pageSize;

    public GetTaskInstancePageCmd(TaskInstanceQuery query, int index, int pageSize) {
        super(query);
        this.index = index;
        this.pageSize = pageSize;
    }

    @Override
    public Page<TaskInstance> execute(CommandContext commandContext) {
        org.camunda.bpm.engine.task.TaskQuery query = convert(commandContext);
        return new Page<>(
                index * pageSize,
                pageSize,
                query.count(),
                () -> query.listPage(index * pageSize, pageSize)
                        .stream()
                        .map(t -> convert((TaskEntity) t, commandContext))
                        .collect(Collectors.toList())
        );
    }
}
