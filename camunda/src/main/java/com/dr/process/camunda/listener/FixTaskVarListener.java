package com.dr.process.camunda.listener;

import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.organise.service.OrganisePersonService;
import com.dr.framework.core.process.service.ProcessConstants;
import com.dr.process.camunda.parselistener.FixTransitionBpmnParseListener;
import org.camunda.bpm.engine.ActivityTypes;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.impl.bpmn.helper.BpmnProperties;
import org.camunda.bpm.engine.impl.core.model.Properties;
import org.camunda.bpm.engine.impl.identity.Authentication;
import org.camunda.bpm.engine.impl.pvm.PvmActivity;
import org.camunda.bpm.engine.impl.pvm.PvmTransition;
import org.camunda.bpm.engine.impl.pvm.delegate.ActivityExecution;
import org.camunda.bpm.engine.variable.value.TypedValue;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

import static com.dr.framework.core.process.service.ProcessConstants.TASK_OWNER_NAME_KEY;
import static com.dr.process.camunda.listener.FixTaskVarListener.BEAN_NAME;

/**
 * 用来在流程运行的时候绑定自定义变量
 *
 * @author dr
 */
@Component(BEAN_NAME)
public class FixTaskVarListener implements TaskListener {
    public static final String BEAN_NAME = "FixVarListener";
    private IdentityService identityService;
    private OrganisePersonService organisePersonService;
    private TaskService taskService;

    public FixTaskVarListener(IdentityService identityService, OrganisePersonService organisePersonService, TaskService taskService) {
        this.identityService = identityService;
        this.organisePersonService = organisePersonService;
        this.taskService = taskService;
    }

    /**
     * 环节启动监听
     *
     * @param delegateTask
     */
    @Override
    public void notify(DelegateTask delegateTask) {
        switch (delegateTask.getEventName()) {
            case EVENTNAME_ASSIGNMENT:
                bindAssignment(delegateTask);
                break;
            case EVENTNAME_CREATE:
            case EVENTNAME_COMPLETE:
                fixComment(delegateTask);
                break;
        }
    }

    /**
     * 绑定任务分发事件
     *
     * @param delegateTask
     */
    private void bindAssignment(DelegateTask delegateTask) {
        Authentication authentication = identityService.getCurrentAuthentication();
        if (authentication != null && !StringUtils.isEmpty(authentication.getUserId())) {
            //绑定任务创建人信息
            Person person = organisePersonService.getPersonById(authentication.getUserId());
            delegateTask.setOwner(authentication.getUserId());
            if (person != null) {
                delegateTask.setVariableLocal(TASK_OWNER_NAME_KEY, person.getUserName());
            }
        }
        //绑定任务接收人变量
        String assignee = delegateTask.getAssignee();
        if (!StringUtils.isEmpty(assignee)) {
            Person person = organisePersonService.getPersonById(assignee);
            if (person != null) {
                //绑定任务接收人名称
                delegateTask.setVariableLocal(ProcessConstants.TASK_ASSIGNEE_NAME_KEY, person.getUserName());
            }
        }
    }

    /**
     * 补全审核意见
     *
     * @param delegateTask
     */
    private void fixComment(DelegateTask delegateTask) {
        DelegateExecution execution = delegateTask.getExecution();
        if (execution instanceof ActivityExecution) {
            PvmActivity activity = ((ActivityExecution) execution).getActivity();
            List<PvmTransition> transitions = activity.getIncomingTransitions()
                    .stream().filter(t -> FixTransitionBpmnParseListener.filter(t, false))
                    .collect(Collectors.toList());
            boolean fromStart = false;
            for (PvmTransition transition : transitions) {
                if (transition.getSource() != null) {
                    Properties properties = transition.getSource().getProperties();
                    if (properties != null) {
                        String type = properties.get(BpmnProperties.TYPE);
                        if (ActivityTypes.START_EVENT.equals(type)) {
                            fromStart = true;
                            break;
                        }
                    }
                }
            }
            TypedValue typedValue;
            if (fromStart) {
                //起始节点从全局取意见
                typedValue = execution.getVariableTyped(ProcessConstants.VAR_COMMENT_KEY);
            } else {
                //其他环节从当前环节变量取意见
                typedValue = execution.getVariableLocalTyped(ProcessConstants.VAR_COMMENT_KEY);
            }
            if (typedValue != null) {
                String comment = (String) typedValue.getValue();
                if (StringUtils.hasText(comment)) {
                    taskService.createComment(delegateTask.getId(), execution.getProcessInstanceId(), comment);
                }
            }
        }
    }

    public IdentityService getIdentityService() {
        return identityService;
    }

    public void setIdentityService(IdentityService identityService) {
        this.identityService = identityService;
    }

    public OrganisePersonService getOrganisePersonService() {
        return organisePersonService;
    }

    public void setOrganisePersonService(OrganisePersonService organisePersonService) {
        this.organisePersonService = organisePersonService;
    }

}
