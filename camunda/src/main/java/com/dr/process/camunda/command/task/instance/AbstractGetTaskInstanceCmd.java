package com.dr.process.camunda.command.task.instance;

import com.dr.framework.core.process.bo.TaskInstance;
import com.dr.process.camunda.command.TaskInstanceUtils;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;
import org.camunda.bpm.engine.impl.persistence.entity.TaskEntity;
import org.camunda.bpm.engine.task.Task;

import java.util.Map;

import static com.dr.framework.core.process.service.ProcessConstants.*;
import static com.dr.process.camunda.command.process.definition.AbstractProcessDefinitionCmd.filter;
import static com.dr.process.camunda.command.process.definition.AbstractProcessDefinitionCmd.getProperty;

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

    public AbstractGetTaskInstanceCmd(boolean withVariables, boolean withProcessVariables, boolean withProperties, boolean withProcessProperty) {
        this.withVariables = withVariables;
        this.withProcessVariables = withProcessVariables;
        this.withProperties = withProperties;
        this.withProcessProperty = withProcessProperty;
    }

    protected TaskInstance convert(Task task, CommandContext commandContext) {
        return TaskInstanceUtils.newTaskInstance(task, commandContext, withVariables, withProcessVariables, withProperties, withProcessProperty);
    }

    public void setWithProcessVariables(boolean withProcessVariables) {
        this.withProcessVariables = withProcessVariables;
    }

}
