package com.dr.process.camunda.listener;

import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.organise.service.OrganisePersonService;
import com.dr.framework.core.process.service.ProcessConstants;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.impl.identity.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

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

    public FixTaskVarListener(IdentityService identityService, OrganisePersonService organisePersonService) {
        this.identityService = identityService;
        this.organisePersonService = organisePersonService;
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
