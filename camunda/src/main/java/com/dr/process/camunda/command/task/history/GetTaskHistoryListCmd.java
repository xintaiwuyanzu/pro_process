package com.dr.process.camunda.command.task.history;

import com.dr.framework.core.process.bo.TaskInstance;
import com.dr.framework.core.process.query.TaskQuery;
import com.dr.process.camunda.command.task.history.AbstractGetTaskHistoryCmd;
import org.camunda.bpm.engine.impl.interceptor.Command;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 获取任务历史列表
 *
 * @author dr
 */
public class GetTaskHistoryListCmd extends AbstractGetTaskHistoryCmd implements Command<List<TaskInstance>> {

    public GetTaskHistoryListCmd(TaskQuery query) {
        super(query);
    }

    @Override
    public List<TaskInstance> execute(CommandContext commandContext) {
        return convert(commandContext)
                .list()
                .stream()
                .map(o -> convert(o, commandContext))
                .collect(Collectors.toList());
    }
}
