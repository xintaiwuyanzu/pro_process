package com.dr.process.camunda.command.task;

import com.dr.framework.core.process.query.TaskQuery;
import com.dr.framework.core.process.service.ProcessConstants;
import com.dr.process.camunda.annotations.SqlProxy;
import org.camunda.bpm.engine.impl.TaskQueryImpl;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;
import org.camunda.bpm.engine.impl.interceptor.CommandExecutor;
import org.springframework.util.StringUtils;

/**
 * @author dr
 */
public class AbstractGetTaskQueryCmd extends AbstractGetTaskCmd {
    TaskQuery query;

    public AbstractGetTaskQueryCmd(TaskQuery query) {
        super(query.isWithVariables(), query.isWithProperty(), query.isWithProcessProperty());
        this.query = query;
    }

    protected org.camunda.bpm.engine.task.TaskQuery convert(CommandContext commandContext) {
        TaskQueryImplWithExtend taskQuery = new TaskQueryImplWithExtend(commandContext.getProcessEngineConfiguration().getCommandExecutorTxRequired());
        taskQuery.initializeFormKeys();
        if (query != null) {
            if (!StringUtils.isEmpty(query.getCreatePerson())) {
                taskQuery.processVariableValueEquals(ProcessConstants.CREATE_KEY, query.getCreatePerson());
            }
            if (!StringUtils.isEmpty(query.getOwner())) {
                taskQuery.taskOwner(query.getOwner());
                //TODO
                //taskQuery.taskVariableValueEquals(ProcessService.OWNER_KEY, query.getOwner());
            }
            if (!StringUtils.isEmpty(query.getAssignee())) {
                taskQuery.taskAssignee(query.getAssignee());
            }
            if (!StringUtils.isEmpty(query.getTitle())) {
                taskQuery.taskNameLike(query.getTitle());
            }
            if (!StringUtils.isEmpty(query.getDescription())) {
                taskQuery.taskDescriptionLike(query.getDescription());
            }
            if (!StringUtils.isEmpty(query.getProcessInstanceId())) {
                taskQuery.processInstanceId(query.getProcessInstanceId());
            }
            if (!StringUtils.isEmpty(query.getTaskKeyLike())) {
                taskQuery.taskDefinitionKeyLike(query.getTaskKeyLike());
            }
            if (!StringUtils.isEmpty(query.getTaskKeyNotLike())) {
                taskQuery.taskDefinitionKeyNotLike(query.getTaskKeyNotLike());
            }
            if (!StringUtils.isEmpty(query.getProcessDefinitionKey())) {
                taskQuery.processDefinitionKeyLike(query.getProcessDefinitionKey());
            }
        }
        taskQuery.orderByTaskCreateTime().desc();
        return taskQuery;
    }


    @SqlProxy(methodName = "selectList", originalSql = "selectTaskByQueryCriteria", proxySql = "selectTaskByQueryCriteriaCustom")
    @SqlProxy(methodName = "selectOne", originalSql = "selectTaskCountByQueryCriteria", proxySql = "selectTaskCountByQueryCriteriaCustom")
    public static class TaskQueryImplWithExtend extends TaskQueryImpl {
        //环节定义key模糊查询
        private String taskDefinitionKeyNotLike;
        //流程定义key模糊查询
        private String processDefinitionKeyLike;

        public TaskQueryImplWithExtend(CommandExecutor commandExecutor) {
            super(commandExecutor);
        }

        public TaskQueryImplWithExtend taskDefinitionKeyNotLike(String taskDefinitionKeyNotLike) {
            this.taskDefinitionKeyNotLike = taskDefinitionKeyNotLike;
            return this;
        }

        public TaskQueryImplWithExtend processDefinitionKeyLike(String processDefinitionKeyLike) {
            this.processDefinitionKeyLike = processDefinitionKeyLike;
            return this;
        }

        public String getProcessDefinitionKeyLike() {
            return processDefinitionKeyLike;
        }

        public String getTaskDefinitionKeyNotLike() {
            return taskDefinitionKeyNotLike;
        }
    }

}
