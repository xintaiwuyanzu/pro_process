package com.dr.process.camunda.command.task.definition;

import com.dr.framework.core.process.bo.TaskDefinition;
import org.camunda.bpm.engine.impl.interceptor.Command;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;
import org.camunda.bpm.engine.impl.persistence.entity.TaskEntity;
import org.camunda.bpm.engine.impl.pvm.PvmTransition;
import org.camunda.bpm.engine.impl.pvm.process.ActivityImpl;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 根据流程实例Id获取当前实例下一环节定义
 *
 * @author dr
 */
public class GetNextTaskDefinitionCmd extends AbstractGetTaskDefinitionCmd implements Command<List<TaskDefinition>> {

    private String taskId;
    private boolean all = false;

    public GetNextTaskDefinitionCmd(String taskId) {
        super(true, true, false);
        this.taskId = taskId;
    }

    public GetNextTaskDefinitionCmd(String taskId, boolean all) {
        super(true, true, false);
        this.taskId = taskId;
        this.all = all;
    }

    @Override
    public List<TaskDefinition> execute(CommandContext commandContext) {
        TaskEntity task = commandContext.getTaskManager().findTaskById(taskId);
        ActivityImpl activity = task.getProcessDefinition().findActivity(task.getTaskDefinitionKey());
        if (activity != null) {
            return activity.getOutgoingTransitions()
                    .stream()
                    .filter(this::filter)
                    .map(o -> convert((ActivityImpl) o.getDestination(), commandContext))
                    .collect(Collectors.toList());
        }
        return null;
    }

    /**
     * 过滤环节连接点
     *
     * @param transition
     * @return
     */
    private boolean filter(PvmTransition transition) {
        if (!all) {
            //过滤掉自定义的连接点
            if (transition.getProperty("fix") != null && (Boolean) transition.getProperty("fix")) {
                return false;
            }
        }
        return true;
    }

}
