package com.dr.process.camunda.command.task;

import com.dr.framework.core.process.bo.TaskInstance;
import org.camunda.bpm.engine.impl.interceptor.Command;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * @author dr
 */
public class GetTaskCmd extends AbstractGetTaskCmd implements Command<TaskInstance> {
    private String taskId;

    public GetTaskCmd(boolean withProperties, String taskId) {
        super(withProperties);
        this.taskId = taskId;
    }

    public GetTaskCmd(String taskId) {
        super(true, true, true);
        this.taskId = taskId;
    }

    @Override
    public TaskInstance execute(CommandContext commandContext) {
        Assert.isTrue(!StringUtils.isEmpty(taskId), "任务Id不能为空!");
        return convert(commandContext.getTaskManager().findTaskById(taskId), commandContext);
    }

}
