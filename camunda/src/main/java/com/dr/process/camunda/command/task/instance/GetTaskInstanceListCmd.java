package com.dr.process.camunda.command.task.instance;

import com.dr.framework.core.process.bo.TaskInstance;
import com.dr.framework.core.process.query.TaskQuery;
import com.dr.process.camunda.command.task.instance.AbstractGetTaskQueryCmd;
import org.camunda.bpm.engine.impl.interceptor.Command;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author dr
 */
public class GetTaskInstanceListCmd extends AbstractGetTaskQueryCmd implements Command<List<TaskInstance>> {

    public GetTaskInstanceListCmd(TaskQuery query) {
        super(query);
    }

    @Override
    public List<TaskInstance> execute(CommandContext commandContext) {
        return convert(commandContext)
                .list()
                .stream()
                .map(t -> convert(t, commandContext))
                .collect(Collectors.toList());
    }
}
