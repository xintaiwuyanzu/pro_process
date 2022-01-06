package com.dr.process.camunda.command.task.instance;

import com.dr.framework.core.process.bo.TaskInstance;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;
import org.camunda.bpm.engine.impl.persistence.entity.TaskEntity;
import org.camunda.bpm.engine.task.Task;

import java.util.Map;

import static com.dr.framework.core.process.service.ProcessConstants.*;
import static com.dr.process.camunda.command.process.definition.AbstractProcessDefinitionCmd.filter;
import static com.dr.process.camunda.command.process.definition.AbstractProcessDefinitionCmd.getProperty;

/**
 *
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

    public AbstractGetTaskInstanceCmd(boolean withProperties) {
        this.withProperties = withProperties;
    }

    public AbstractGetTaskInstanceCmd(boolean withVariables, boolean withProperties, boolean withProcessProperty) {
        this.withVariables = withVariables;
        this.withProperties = withProperties;
        this.withProcessProperty = withProcessProperty;
    }

    protected TaskInstance convert(Task task, CommandContext commandContext) {
        if (task == null) {
            return null;
        }
        TaskInstance to = new TaskInstance();
        Map<String, Object> variables = commandContext
                .getProcessEngineConfiguration()
                .getTaskService()
                .getVariables(task.getId());
        to.setAssignee(task.getAssignee());
        to.setAssigneeName((String) variables.get(ASSIGNEE_NAME_KEY));

        to.setCreateDate(task.getCreateTime().getTime());

        if (task instanceof TaskEntity) {
            ((TaskEntity) task).initializeFormKey();
        }
        to.setFormKey(task.getFormKey());
        to.setId(task.getId());
        to.setProcessInstanceId(task.getProcessInstanceId());
        to.setProcessDefineId(task.getProcessDefinitionId());
        to.setTaskDefineKey(task.getTaskDefinitionKey());
        to.setOwner(task.getOwner());

        to.setOwnerName((String) variables.get(OWNER_NAME_KEY));

        to.setCreatePerson((String) variables.get(PROCESS_CREATE_PERSON_KEY));

        to.setCreatePersonName((String) variables.get(PROCESS_CREATE_NAME_KEY));

        to.setName(task.getName());
        to.setDescription(task.getDescription());

        to.setSuspend(task.isSuspended());
        if (withVariables) {
            to.setVariables(filter(variables));
        }
        if (withProcessVariables) {
            //TODO
        }
        if (withProperties) {
            to.setProPerties(getProperty(task.getProcessDefinitionId(),
                    task.getTaskDefinitionKey(),
                    commandContext));
        }
        if (withProcessProperty) {
            to.setProcessProPerties(getProperty(task.getProcessDefinitionId(),
                    task.getProcessDefinitionId().split(":")[0],
                    commandContext));
        }
        return to;
    }

    public void setWithProcessVariables(boolean withProcessVariables) {
        this.withProcessVariables = withProcessVariables;
    }

}
