package com.dr.process.camunda.command.task.instance;

import com.dr.framework.core.process.bo.TaskInstance;
import com.dr.process.camunda.command.TaskInstanceUtils;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;
import org.camunda.bpm.engine.impl.persistence.entity.TaskEntity;

/**
 * @author dr
 */
public class AbstractGetTaskInstanceCmd {
    /**
     * 包含环节实例变量
     */
    private boolean withVariables;
    /**
     * 包含流程实例变量
     */
    private boolean withProcessVariables;
    /**
     * 包含环节扩展属性定义
     */
    private boolean withProperties;
    /**
     * 包含流程扩展属性定义
     */
    private boolean withProcessProperty;
    private boolean withComments;

    public AbstractGetTaskInstanceCmd(boolean withVariables, boolean withProcessVariables, boolean withProperties, boolean withProcessProperty) {
        this(withVariables, withProcessVariables, withProperties, withProcessProperty, false);
    }

    public AbstractGetTaskInstanceCmd(boolean withVariables, boolean withProcessVariables, boolean withProperties, boolean withProcessProperty, boolean withComments) {
        this.withVariables = withVariables;
        this.withProcessVariables = withProcessVariables;
        this.withProperties = withProperties;
        this.withProcessProperty = withProcessProperty;
        this.withComments = withComments;
    }

    protected TaskInstance convert(TaskEntity task, CommandContext commandContext) {
        return TaskInstanceUtils.newTaskInstance(task, commandContext, withVariables, withProcessVariables, withProperties, withProcessProperty, withComments);
    }

    public void setWithProcessVariables(boolean withProcessVariables) {
        this.withProcessVariables = withProcessVariables;
    }

}
