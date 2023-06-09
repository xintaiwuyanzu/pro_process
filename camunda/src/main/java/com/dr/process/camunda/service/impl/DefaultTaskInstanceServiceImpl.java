package com.dr.process.camunda.service.impl;

import com.dr.framework.common.page.Page;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.process.bo.*;
import com.dr.framework.core.process.query.TaskInstanceQuery;
import com.dr.framework.core.process.service.ProcessContext;
import com.dr.framework.core.process.service.ProcessTypeProvider;
import com.dr.framework.core.process.service.TaskContext;
import com.dr.framework.core.process.service.TaskInstanceService;
import com.dr.process.camunda.command.comment.GetProcessCommentsCmd;
import com.dr.process.camunda.command.comment.GetTaskCommentsCmd;
import com.dr.process.camunda.command.process.instance.ConvertProcessInstanceCmd;
import com.dr.process.camunda.command.task.definition.GetNextTaskDefinitionCmd;
import com.dr.process.camunda.command.task.history.GetTaskHistoryListCmd;
import com.dr.process.camunda.command.task.history.GetTaskHistoryPageCmd;
import com.dr.process.camunda.command.task.instance.GetTaskInstanceListCmd;
import com.dr.process.camunda.command.task.instance.GetTaskInstancePageCmd;
import com.dr.process.camunda.parselistener.FixTransitionBpmnParseListener;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.camunda.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.camunda.bpm.engine.impl.persistence.entity.ProcessInstanceWithVariablesImpl;
import org.camunda.bpm.engine.impl.persistence.entity.TaskEntity;
import org.camunda.bpm.engine.impl.pvm.process.ActivityImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.dr.framework.core.process.service.ProcessConstants.*;

/**
 * 默认环节实例实现
 *
 * @author dr
 */
@Service
public class DefaultTaskInstanceServiceImpl extends BaseProcessServiceImpl implements TaskInstanceService {

    private Map<String, ProcessTypeProvider> processTypeProviderMap;

    @Override
    public List<TaskInstance> taskList(TaskInstanceQuery query) {
        return getCommandExecutor().execute(new GetTaskInstanceListCmd(query));
    }

    @Override
    public Page<TaskInstance> taskPage(TaskInstanceQuery query, int index, int pageSize) {
        return getCommandExecutor().execute(new GetTaskInstancePageCmd(query, index, pageSize));
    }

    @Override
    public List<TaskInstance> taskHistoryList(TaskInstanceQuery query) {
        return getCommandExecutor().execute(new GetTaskHistoryListCmd(query));
    }

    @Override
    public Page<TaskInstance> taskHistoryPage(TaskInstanceQuery query, int index, int pageSize) {
        return getCommandExecutor().execute(new GetTaskHistoryPageCmd(query, index, pageSize));
    }

    @Override
    public Map<String, Object> getProcessVariables(String taskId) {
        return getTaskService().getVariables(taskId);
    }

    @Override
    public void setProcessVariable(String taskId, Map<String, Object> variables) {
        getTaskService().setVariables(taskId, variables);
    }

    @Override
    public void removeProcessVariable(String taskId, String... variNames) {
        List<String> strings = Arrays.asList(variNames);
        getTaskService().removeVariables(taskId, strings);
    }

    @Override
    public Map<String, Object> getTaskVariables(String taskId) {
        return getTaskService().getVariablesLocal(taskId);
    }

    @Override
    public void setTaskVariable(String taskId, Map<String, Object> variables) {
        getTaskService().setVariablesLocal(taskId, variables);
    }

    @Override
    public void removeTaskVariable(String taskId, String... variNames) {
        List<String> strings = Arrays.asList(variNames);
        getTaskService().removeVariablesLocal(taskId, strings);
    }

    @Override
    public List<Comment> taskComments(String taskId) {
        return getCommandExecutor().execute(new GetTaskCommentsCmd(taskId, getOrganisePersonService()));
    }

    @Override
    public List<Comment> processComments(String processInstanceId) {
        return getCommandExecutor().execute(new GetProcessCommentsCmd(processInstanceId, getOrganisePersonService()));
    }

    @Override
    public void addComment(String taskId, String... comments) {
        TaskInstance taskObject = taskInfo(taskId);
        for (String comment : comments) {
            getTaskService().createComment(taskObject.getId(), taskObject.getProcessInstanceId(), comment);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProcessInstance start(String processDefinitionId, Map<String, Object> variMap, Person person) {
        ProcessDefinition processDefinition = getProcessDefinitionService().getProcessDefinitionById(processDefinitionId);
        ProcessContext context = buildTaskContext(processDefinition, person, variMap);
        ProcessTypeProvider processTypeProvider = processTypeProviderMap.get(processDefinition.getType());

        Assert.isTrue(processTypeProvider != null, "不支持指定的流程类型：" + processDefinition.getType());
        //启动前拦截
        processTypeProvider.onBeforeStartProcess(context);

        //详情页面URL地址
        if (!context.getProcessVarMap().containsKey(PROCESS_FORM_URL_KEY)) {
            //先尝试从流程定义中获取表单地址，代码是死的，配置是灵活的
            Property proPerty = processDefinition.getProPerty(PROCESS_FORM_URL_KEY);
            if (proPerty != null) {
                context.addVar(PROCESS_FORM_URL_KEY, proPerty.getValue());
            } else {
                String providerFormUrl = processTypeProvider.getFormUrl(context);
                if (StringUtils.hasText(providerFormUrl)) {
                    context.addVar(PROCESS_FORM_URL_KEY, providerFormUrl);
                }
            }
        }

        //流程实例标题
        if (StringUtils.hasText(context.getProcessInstanceTitle())) {
            context.addVar(PROCESS_TITLE_KEY, context.getProcessInstanceTitle());
        } else {
            context.addVar(PROCESS_TITLE_KEY, processDefinition.getName() + DateFormatUtils.format(new Date(), "YYYY-MM-DD"));
        }
        //流程实例详情描述
        if (StringUtils.hasText(context.getProcessInstanceDetail())) {
            //业务自定义流程实例标题
            context.addVar(PROCESS_DETAIL_KEY, context.getProcessInstanceDetail());
        }
        //设置流程任务类型变量
        context.addVar(PROCESS_TYPE_KEY, processTypeProvider.getType());

        if (!context.getProcessVarMap().containsKey(TASK_ASSIGNEE_KEY)) {
            //设置启动环节任务人为传进来的登陆人信息
            context.addVar(TASK_ASSIGNEE_KEY, person.getId());
        }
        //环节审核意见
        if (StringUtils.hasText((String) context.getBusinessParams().get("comment"))) {
            //业务自定义流程实例标题
            context.addVar(VAR_COMMENT_KEY, context.getBusinessParams().get("comment"));
        }
        //调用流程引擎启动流程
        ProcessInstanceWithVariablesImpl instance = (ProcessInstanceWithVariablesImpl) getRuntimeService().startProcessInstanceById(processDefinition.getId(), context.getBusinessId(), context.getProcessVarMap());
        //转换流程实例对象
        ProcessInstance instance1 = getCommandExecutor().execute(new ConvertProcessInstanceCmd(instance.getId(), instance.getVariables()));

        context.setProcessInstance(instance1);
        //启动后拦截
        processTypeProvider.onAfterStartProcess(context);
        //如果直接启动到第二环节
        if (variMap.containsKey(VAR_COMPLETE_TO_NEXT)) {
            //自动发送到第二个环节
            boolean isAuto = false;
            try {
                isAuto = Boolean.parseBoolean((String) variMap.get(VAR_COMPLETE_TO_NEXT));
            } catch (Exception ignore) {
            }
            if (isAuto) {
                //当前流程实例对应的所有的环节实例
                List<TaskEntity> taskEntities = instance.getExecutionEntity().getTasks();
                if (taskEntities.size() == 1) {
                    List<TaskDefinition> next = commandExecutor.execute(new GetNextTaskDefinitionCmd(taskEntities.get(0).getId()));
                    if (next != null && next.size() == 1) {
                        //读取下一个
                        variMap.put(VAR_NEXT_TASK_ID, next.get(0).getId());
                        complete(taskEntities.get(0).getId(), variMap, person);
                    }
                }
            }
        }
        return instance1;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void complete(String taskId, Map<String, Object> variables, Person person) {
        //构造上下文
        TaskContext context = buildTaskContext(taskId, variables, person);
        //找到typeProvider
        ProcessTypeProvider processTypeProvider = processTypeProviderMap.get(context.getProcessDefinition().getType());
        Assert.isTrue(processTypeProvider != null, "不支持指定的流程类型：" + context.getProcessDefinition().getType());
        processTypeProvider.onBeforeCompleteTask(context);
        getTaskService().complete(taskId, context.getProcessVarMap());
        processTypeProvider.onAfterCompleteTask(context);
    }

    @Override
    public void suspend(String processInstanceId) {
        Assert.isTrue(!StringUtils.isEmpty(processInstanceId), "流程实例不能为空");
        getRuntimeService().suspendProcessInstanceById(processInstanceId);
    }

    @Override
    public void active(String processInstanceId) {
        Assert.isTrue(!StringUtils.isEmpty(processInstanceId), "流程实例不能为空");
        getRuntimeService().activateProcessInstanceById(processInstanceId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void endProcess(String taskId, Map<String, Object> variables, Person person) {
        //构造上下文
        TaskContext context = buildTaskContext(taskId, variables, person);
        //找到typeProvider
        ProcessTypeProvider processTypeProvider = processTypeProviderMap.get(context.getProcessDefinition().getType());
        Assert.isTrue(processTypeProvider != null, "不支持指定的流程类型：" + context.getProcessDefinition().getType());
        processTypeProvider.onBeforeEndProcess(context);
        String nextId = (String) variables.get(VAR_NEXT_TASK_ID);
        if (!StringUtils.hasText(nextId)) {
            //如果没有指定下一环节
            ProcessDefinitionEntity pd = (ProcessDefinitionEntity) getProcessEngineConfiguration().getRepositoryService().getProcessDefinition(context.getProcessDefinition().getId());
            List<ActivityImpl> activities = pd.getActivities().stream().filter(FixTransitionBpmnParseListener::isEndTask).collect(Collectors.toList());
            Assert.isTrue(activities.size() == 1, "流程定义多个办结节点，请指定节点");
            context.addVar(VAR_NEXT_TASK_ID, activities.get(0).getActivityId());
        }
        getTaskService().complete(taskId, context.getProcessVarMap());

        processTypeProvider.onAfterEndProcess(context);
    }

    @Override
    public void afterPropertiesSet() {
        processTypeProviderMap = getApplicationContext().getBeansOfType(ProcessTypeProvider.class).values().stream().collect(Collectors.toMap(ProcessTypeProvider::getType, t -> t));
    }


}
