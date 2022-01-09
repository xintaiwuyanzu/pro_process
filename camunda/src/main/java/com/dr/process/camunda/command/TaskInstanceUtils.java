package com.dr.process.camunda.command;

import com.dr.framework.core.process.bo.ProcessInstance;
import com.dr.framework.core.process.bo.TaskInstance;
import com.dr.framework.core.process.service.ProcessConstants;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.history.HistoricVariableInstance;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;
import org.camunda.bpm.engine.impl.persistence.entity.HistoricTaskInstanceEntity;
import org.camunda.bpm.engine.impl.persistence.entity.TaskEntity;

import java.util.HashMap;
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
    public static TaskInstance newTaskInstance(TaskEntity task, CommandContext commandContext,
                                               boolean withVariables,
                                               boolean withProcessVariables,
                                               boolean withProperties,
                                               boolean withProcessProperty) {
        if (task == null) {
            return null;
        }

        TaskInstance to = new TaskInstance();
        //主键
        to.setId(task.getId());
        //环节任务接收人Id
        to.setAssignee(task.getAssignee());
        //环节创建时间
        to.setCreateDate(task.getCreateTime().getTime());
        //环节表单Id，这个是从环节定义中获取的
        //to.setFormKey(task.getFormKey());
        //流程实例Id
        to.setProcessInstanceId(task.getProcessInstanceId());
        //流程定义Id
        to.setProcessDefineId(task.getProcessDefinitionId());
        //环节定义编码
        to.setTaskDefineKey(task.getTaskDefinitionKey());
        //任务发送人Id
        to.setOwner(task.getOwner());
        //任务定义名称
        to.setName(task.getName());
        //任务定义描述
        to.setDescription(task.getDescription());
        //任务状态
        to.setSuspend(task.isSuspended());

        //这里会查询所有的环境变量，包括环节和流程的变量
        Map<String, Object> variables = task.getVariables();
        //绑定环境变量
        bindVaris(variables, to, withVariables, withProcessVariables);
        //绑定定义属性
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


    public static TaskInstance newTaskInstance(HistoricTaskInstanceEntity his, CommandContext commandContext,
                                               boolean withVariables,
                                               boolean withProcessVariables,
                                               boolean withProperties,
                                               boolean withProcessProperty) {
        if (his == null) {
            return null;
        }
        TaskInstance to = new TaskInstance();
        to.setId(his.getId());
        to.setAssignee(his.getAssignee());
        to.setCreateDate(his.getStartTime().getTime());
        to.setProcessInstanceId(his.getProcessInstanceId());
        to.setProcessDefineId(his.getProcessDefinitionId());
        to.setTaskDefineKey(his.getTaskDefinitionKey());
        to.setOwner(his.getOwner());
        to.setName(his.getName());
        to.setDescription(his.getDescription());

        HistoryService historyService = commandContext.getProcessEngineConfiguration().getHistoryService();

        Map<String, Object> taskVaris = new HashMap<>();
        historyService
                .createHistoricVariableInstanceQuery()
                .taskIdIn(his.getId())
                .list()
                .forEach(h -> taskVaris.put(h.getName(), h.getValue()));
        bindVaris(taskVaris, to, withVariables, false);

        Map<String, Object> processVaris = new HashMap<>();
        historyService
                .createHistoricVariableInstanceQuery()
                .processInstanceId(his.getProcessInstanceId())
                .list()
                .forEach(h -> processVaris.put(h.getName(), h.getValue()));

        bindVaris(processVaris, to, false, withProcessVariables);

        if (withProperties) {
            to.setProPerties(getProperty(his.getProcessDefinitionId(),
                    his.getTaskDefinitionKey(),
                    commandContext));
        }
        if (withProcessProperty) {
            to.setProcessProPerties(getProperty(his.getProcessDefinitionId(),
                    his.getProcessDefinitionId().split(":")[0],
                    commandContext));
        }
        return to;
    }

    private static void bindVaris(Map<String, Object> variables, TaskInstance to, boolean withVariables, boolean withProcessVariables) {
        //先绑定流程相关变量
        bindVaris(variables, to);
        //接收人名称
        to.setAssigneeName((String) variables.get(TASK_ASSIGNEE_NAME_KEY));
        //任务发送人名称
        to.setOwnerName((String) variables.get(TASK_OWNER_NAME_KEY));

        if (withVariables) {
            to.setVariables(filter(variables));
        }
        if (withProcessVariables) {
            //TODO
            to.setProcessVariables(filter(variables));
        }
    }


    public static void bindVaris(Map<String, Object> variables, ProcessInstance po) {
        if (variables != null) {
            po.setTitle((String) variables.get(ProcessConstants.PROCESS_TITLE_KEY));
            po.setDetail((String) variables.get(ProcessConstants.PROCESS_DETAIL_KEY));
            //创建人Id
            po.setCreatePerson((String) variables.get(PROCESS_CREATE_PERSON_KEY));
            //创建人名称
            po.setCreatePersonName((String) variables.get(ProcessConstants.PROCESS_CREATE_NAME_KEY));
            po.setType((String) variables.get(ProcessConstants.PROCESS_TYPE_KEY));
            po.setFormUrl((String) variables.get(ProcessConstants.PROCESS_FORM_URL_KEY));
        }
    }


}
