package com.dr.process.camunda.command;

import com.dr.framework.core.process.query.ProcessInstanceQuery;
import com.dr.framework.core.process.query.TaskInstanceQuery;
import com.dr.framework.core.process.service.ProcessConstants;
import org.camunda.bpm.engine.history.HistoricProcessInstanceQuery;
import org.camunda.bpm.engine.history.HistoricTaskInstanceQuery;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;
import org.camunda.bpm.engine.task.TaskQuery;
import org.springframework.util.StringUtils;

/**
 * 查询方法转换工具类
 * 转换查询方法工具类，所有相关的转换都放在一起，省的别的地方漏了
 *
 * @author dr
 */
public class QueryUtils {
    /**
     * 查询流程历史实例
     *
     * @param commandContext
     * @param query
     * @return
     */
    public static HistoricProcessInstanceQuery processHistoryQuery(CommandContext commandContext, ProcessInstanceQuery query) {
        HistoricProcessInstanceQuery hq = commandContext.getProcessEngineConfiguration().getHistoryService().createHistoricProcessInstanceQuery();
        if (query != null) {
            if (StringUtils.hasText(query.getTitle())) {
                //根据标题查询流程实例
                hq.variableValueLike(ProcessConstants.PROCESS_TITLE_KEY, "%" + query.getTitle() + "%S");
            }
            if (StringUtils.hasText(query.getDetail())) {
                //根据描述查询流程实例
                hq.variableValueLike(ProcessConstants.PROCESS_DETAIL_KEY, query.getDetail());
            }
            if (StringUtils.hasText(query.getType())) {
                //根据流程类型查询流程实例
                hq.variableValueLike(ProcessConstants.PROCESS_TYPE_KEY, "%" + query.getType() + "%");
            }
            if (StringUtils.hasText(query.getCreatePerson())) {
                //流程启动人
                hq.startedBy(query.getCreatePerson());
            }
            if (StringUtils.hasText(query.getCreatePersonName())) {
                //任务发起人
                hq.variableValueLike(ProcessConstants.PROCESS_CREATE_NAME_KEY, "%" + query.getCreatePersonName() + "%");
            }
            if (StringUtils.hasText(query.getName())) {
                //任务名称
                hq.variableValueLike(ProcessConstants.PROCESS_TITLE_KEY,"%" + query.getName() + "%");
            }
        }
        return hq;
    }

    /**
     * 根据条件创建环节实例查询
     *
     * @param context
     * @param query
     * @return
     */
    public static TaskQuery taskInstanceQuery(CommandContext context, TaskInstanceQuery query) {
        TaskQuery taskQuery = context.getProcessEngineConfiguration().getTaskService().createTaskQuery();
        if (query != null) {
            if (StringUtils.hasText(query.getTitle())) {
                //根据标题查询流程实例
                taskQuery.processVariableValueLike(ProcessConstants.PROCESS_TITLE_KEY, "%" + query.getTitle() + "%");
            }
            if (StringUtils.hasText(query.getDetail())) {
                //根据描述查询流程实例
                taskQuery.processVariableValueLike(ProcessConstants.PROCESS_DETAIL_KEY, "%" + query.getDetail() + "%");
            }
            if (StringUtils.hasText(query.getType())) {
                //根据流程类型查询流程实例
                taskQuery.processVariableValueLike(ProcessConstants.PROCESS_TYPE_KEY, "%" + query.getType() + "%");
            }
            if (StringUtils.hasText(query.getCreatePerson())) {
                //流程启动人
                taskQuery.processVariableValueEquals(ProcessConstants.PROCESS_CREATE_PERSON_KEY, query.getCreatePerson());
            }
            if (StringUtils.hasText(query.getOwner())) {
                //环节创建人
                taskQuery.taskOwner(query.getOwner());
            }
            if (StringUtils.hasText(query.getAssignee())) {
                //环节受理人
                taskQuery.taskAssignee(query.getAssignee());
            }
            if (StringUtils.hasText(query.getDescription())) {
                //环节描述
                taskQuery.taskDescriptionLike(query.getDescription());
            }
            if (StringUtils.hasText(query.getProcessInstanceId())) {
                //流程实例Id
                taskQuery.processInstanceId(query.getProcessInstanceId());
            }
            if (StringUtils.hasText(query.getTaskKeyLike())) {
                //环节定义Id
                taskQuery.taskDefinitionKeyLike(query.getTaskKeyLike());
            }
            if (StringUtils.hasText(query.getProcessDefinitionKey())) {
                //流程定义key
                taskQuery.processDefinitionKey(query.getProcessDefinitionKey());
            }
        }
        return taskQuery;
    }

    /**
     * 创建环节历史查询对象
     *
     * @param context
     * @param query
     * @return
     */
    public static HistoricTaskInstanceQuery taskHistoryQuery(CommandContext context, TaskInstanceQuery query) {
        HistoricTaskInstanceQuery hq = context.getProcessEngineConfiguration().getHistoryService().createHistoricTaskInstanceQuery();
        if (query != null) {

            if (StringUtils.hasText(query.getTitle())) {
                //根据标题查询流程实例
                hq.processVariableValueLike(ProcessConstants.PROCESS_TITLE_KEY, "%" + query.getTitle() + "%S");
            }
            if (StringUtils.hasText(query.getDetail())) {

                //根据描述查询流程实例
                hq.processVariableValueLike(ProcessConstants.PROCESS_DETAIL_KEY, "%" + query.getDetail() + "%");
            }
            if (StringUtils.hasText(query.getType())) {

                //根据流程类型查询流程实例
                hq.processVariableValueLike(ProcessConstants.PROCESS_TYPE_KEY, "%" + query.getType() + "%");
            }
            if (StringUtils.hasText(query.getCreatePerson())) {
                //流程启动人
                hq.processVariableValueEquals(ProcessConstants.PROCESS_CREATE_PERSON_KEY, query.getCreatePerson());
            }
            if (StringUtils.hasText(query.getOwner())) {
                //环节创建人
                hq.taskOwner(query.getOwner());
            }
            if (StringUtils.hasText(query.getAssignee())) {
                //环节受理人
                hq.taskAssignee(query.getAssignee());
            }
            if (StringUtils.hasText(query.getDescription())) {
                //环节描述
                hq.taskDescriptionLike(query.getDescription());
            }

            if (StringUtils.hasText(query.getProcessInstanceId())) {
                //流程实例Id
                hq.processInstanceId(query.getProcessInstanceId());
            }
            if (StringUtils.hasText(query.getTaskKeyLike())) {
                //环节定义Id
                hq.taskDefinitionKeyIn(query.getTaskKeyLike());
            }
            if (StringUtils.hasText(query.getProcessDefinitionKey())) {
                //流程定义key
                hq.processDefinitionKey(query.getProcessDefinitionKey());
            }
        }
        return hq;
    }


}
