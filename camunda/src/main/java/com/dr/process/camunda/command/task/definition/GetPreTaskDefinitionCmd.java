package com.dr.process.camunda.command.task.definition;

import com.dr.framework.core.process.bo.TaskDefinition;
import com.dr.process.camunda.parselistener.FixTransitionBpmnParseListener;
import org.camunda.bpm.engine.impl.interceptor.Command;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;
import org.camunda.bpm.engine.impl.persistence.entity.TaskEntity;
import org.camunda.bpm.engine.impl.pvm.process.ActivityImpl;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 根据流程实例Id获取当前实例前一环节定义
 *
 * @author dr
 */
public class GetPreTaskDefinitionCmd extends AbstractGetTaskDefinitionCmd implements Command<List<TaskDefinition>> {

    private String taskId;
    private boolean all = false;

    public GetPreTaskDefinitionCmd(String taskId) {
        super(true, true, false);
        this.taskId = taskId;
    }

    public GetPreTaskDefinitionCmd(String taskId, boolean all) {
        super(true, true, false);
        this.taskId = taskId;
        this.all = all;
    }

    @Override
    public List<TaskDefinition> execute(CommandContext commandContext) {
        TaskEntity task = commandContext.getTaskManager().findTaskById(taskId);
        ActivityImpl activity = task.getProcessDefinition().findActivity(task.getTaskDefinitionKey());
        if (activity != null) {
            return activity.getIncomingTransitions()
                    .stream()
                    .filter(t -> FixTransitionBpmnParseListener.filter(t, all))
                    .map(o -> convert((ActivityImpl) o.getDestination(), commandContext))
                    .collect(Collectors.toList());
        }
        return null;
    }


}
