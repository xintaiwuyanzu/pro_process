package com.dr.process.camunda.command.task.definition;

import com.dr.framework.core.process.bo.TaskDefinition;
import com.dr.process.camunda.command.process.definition.AbstractProcessDefinitionCmd;
import org.camunda.bpm.engine.impl.core.model.PropertyKey;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;
import org.camunda.bpm.engine.impl.pvm.process.ActivityImpl;

/**
 * 抽象环节定义父类
 *
 * @author dr
 */
public class AbstractGetTaskDefinitionCmd {
    private boolean withProperty;
    private boolean withProcessProperty;
    //TODO
    private boolean withStartUser;

    //描述定义属性
    static final PropertyKey<String> documentation = new PropertyKey<>("documentation");

    public AbstractGetTaskDefinitionCmd(boolean withProperty, boolean withProcessProperty, boolean withStartUser) {
        this.withProperty = withProperty;
        this.withProcessProperty = withProcessProperty;
        this.withStartUser = withStartUser;
    }

    /**
     * 转换环节定义
     *
     * @param activity
     * @param commandContext
     * @return
     */
    protected TaskDefinition convert(ActivityImpl activity, CommandContext commandContext) {
        if (activity == null) {
            return null;
        }
        TaskDefinition taskDefinition = new TaskDefinition();
        taskDefinition.setId(activity.getId());
        taskDefinition.setName(activity.getName());
        taskDefinition.setDescription(activity.getProperties().get(documentation));

        if (withProperty) {
            taskDefinition.setProPerties(AbstractProcessDefinitionCmd.getProperty(
                    activity.getProcessDefinition().getId(),
                    activity.getId(),
                    commandContext));
        }
        if (withProcessProperty) {
            taskDefinition.setProcessProPerties(AbstractProcessDefinitionCmd.getProperty(
                    activity.getProcessDefinition().getId(),
                    activity.getProcessDefinition().getId().split(":")[0],
                    commandContext));
        }
        return taskDefinition;
    }

}
