package com.dr.process.camunda.command.task.instance;

import com.dr.framework.core.process.bo.TaskInstance;
import org.camunda.bpm.engine.impl.interceptor.Command;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * 根据Id查询环节实例
 *
 * @author dr
 */
public class GetTaskInstanceCmd extends AbstractGetTaskInstanceCmd implements Command<TaskInstance> {
    private String taskId;

    public GetTaskInstanceCmd(String taskId) {
        super(true, true, true, true);
        this.taskId = taskId;
    }

    @Override
    public TaskInstance execute(CommandContext commandContext) {
        Assert.isTrue(!StringUtils.isEmpty(taskId), "任务Id不能为空!");
        return convert(commandContext.getTaskManager().findTaskById(taskId), commandContext);
    }

}
