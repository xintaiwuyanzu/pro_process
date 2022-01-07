package com.dr.process.camunda.command.task;

import com.dr.framework.core.process.service.ProcessConstants;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.impl.interceptor.Command;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;
import org.camunda.bpm.engine.task.Task;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 带备注发送任务给下一环节
 */
public class SendTaskCmd implements Command<Void> {
    private String taskId;
    private String nextPerson;
    private String comment;
    private Map<String, Object> variables;

    public SendTaskCmd(String taskId, String nextPerson, String comment, Map<String, Object> variables) {
        this.taskId = taskId;
        this.nextPerson = nextPerson;
        this.comment = comment;
        this.variables = variables;
    }

    @Override
    public Void execute(CommandContext commandContext) {
        Assert.isTrue(!StringUtils.isEmpty(taskId), "环节Id不能为空！");

        TaskService taskService = commandContext.getProcessEngineConfiguration().getTaskService();

        Task task = commandContext.getTaskManager().findTaskById(taskId);
        if (!StringUtils.isEmpty(comment)) {
            taskService.createComment(taskId, task.getProcessInstanceId(), comment);
        }
        Map<String, Object> map = new HashMap<>();
        if (variables != null) {
            map.putAll(variables);
        }
        if (!StringUtils.isEmpty(nextPerson)) {
            map.put(ProcessConstants.TASK_ASSIGNEE_KEY, nextPerson);
        }
        taskService.complete(taskId, map);
        return null;
    }
}
