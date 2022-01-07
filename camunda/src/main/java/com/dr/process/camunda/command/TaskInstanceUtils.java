package com.dr.process.camunda.command;

import com.dr.framework.core.process.bo.TaskInstance;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.camunda.bpm.engine.history.HistoricVariableInstance;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;
import org.camunda.bpm.engine.impl.persistence.entity.TaskEntity;
import org.camunda.bpm.engine.task.Task;

import java.util.Map;
import java.util.stream.Collectors;

import static com.dr.framework.core.process.service.ProcessConstants.*;
import static com.dr.process.camunda.command.process.definition.AbstractProcessDefinitionCmd.filter;
import static com.dr.process.camunda.command.process.definition.AbstractProcessDefinitionCmd.getProperty;

/**
 * 转换任务历史和任务实例为自定义任务实例对象
 *
 * @author dr
 */
public class TaskInstanceUtils {

    /**
     * 根据camunda环节实例创建对外接口
     *
     * @param task
     * @param commandContext
     * @param withVariables
     * @param withProcessVariables
     * @param withProperties
     * @param withProcessProperty
     * @return
     */
    public static TaskInstance newTaskInstance(Task task, CommandContext commandContext,
                                               boolean withVariables,
                                               boolean withProcessVariables,
                                               boolean withProperties,
                                               boolean withProcessProperty) {
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

    public static TaskInstance newTaskInstance(HistoricTaskInstance his, CommandContext commandContext,
                                               boolean withVariables,
                                               boolean withProcessVariables,
                                               boolean withProperties,
                                               boolean withProcessProperty) {
        if (his == null) {
            return null;
        }
        TaskInstance to = new TaskInstance();

        HistoryService historyService = commandContext.getProcessEngineConfiguration().getHistoryService();

        Map<String, Object> variables = historyService
                .createHistoricVariableInstanceQuery()
                .taskIdIn(his.getId())
                .list()
                .stream()
                .collect(Collectors.toMap(HistoricVariableInstance::getName, HistoricVariableInstance::getValue));
        to.setAssignee(his.getAssignee());
        to.setAssigneeName((String) variables.get(ASSIGNEE_NAME_KEY));

        to.setCreateDate(his.getStartTime().getTime());
        //TODO
        //to.setFormKey(his.getFormKey());
        to.setId(his.getId());
        to.setProcessInstanceId(his.getProcessInstanceId());
        to.setProcessDefineId(his.getProcessDefinitionId());
        to.setTaskDefineKey(his.getTaskDefinitionKey());
        to.setOwner(his.getOwner());

        to.setOwnerName((String) variables.get(OWNER_NAME_KEY));

        to.setCreatePerson((String) variables.get(PROCESS_CREATE_PERSON_KEY));

        to.setCreatePersonName((String) variables.get(PROCESS_CREATE_NAME_KEY));

        to.setName(his.getName());
        to.setDescription(his.getDescription());

        if (withVariables) {
            to.setVariables(filter(variables));
        }
        if (withProcessVariables) {
            //to.setProcessVariables();
        }
        if (withProperties) {
            to.setProPerties(getProperty(his.getProcessDefinitionId(),
                    his.getTaskDefinitionKey(),
                    commandContext));
        }
        return to;
    }

}
