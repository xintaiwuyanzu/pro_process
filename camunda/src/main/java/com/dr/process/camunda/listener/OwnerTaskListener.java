package com.dr.process.camunda.listener;

import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.organise.service.OrganisePersonService;
import com.dr.framework.core.process.service.ProcessConstants;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.impl.identity.Authentication;
import org.camunda.bpm.engine.impl.util.ClockUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import static com.dr.framework.core.process.service.ProcessConstants.OWNER_NAME_KEY;
import static com.dr.process.camunda.listener.OwnerTaskListener.BEAN_NAME;

/**
 * 用来设置任务创建人信息
 *
 * @author dr
 */
@Component(BEAN_NAME)
public class OwnerTaskListener implements TaskListener, ExecutionListener {
    public static final String BEAN_NAME = "OwnerTaskListener";
    private IdentityService identityService;
    private OrganisePersonService organisePersonService;

    public OwnerTaskListener(IdentityService identityService, OrganisePersonService organisePersonService) {
        this.identityService = identityService;
        this.organisePersonService = organisePersonService;
    }

    /**
     * 流程启动监听
     *
     * @param execution
     */
    @Override
    public void notify(DelegateExecution execution) {
        String createPerson = (String) execution.getVariable(ProcessConstants.CREATE_KEY);
        if (!StringUtils.isEmpty(createPerson)) {
            //流程创建人名称
            Person person = organisePersonService.getPersonById(createPerson);
            execution.setVariable(ProcessConstants.CREATE_NAME_KEY, person.getUserName());
        }
        //流程创建时间
        execution.setVariable(ProcessConstants.CREATE_DATE_KEY, ClockUtil.getCurrentTime().getTime());
    }

    /**
     * 环节启动监听
     *
     * @param delegateTask
     */
    @Override
    public void notify(DelegateTask delegateTask) {
        Authentication authentication = identityService.getCurrentAuthentication();
        if (authentication != null && !StringUtils.isEmpty(authentication.getUserId())) {
            //绑定任务创建人信息
            Person person = organisePersonService.getPersonById(authentication.getUserId());
            delegateTask.setOwner(authentication.getUserId());
            if (person != null) {
                delegateTask.setVariableLocal(OWNER_NAME_KEY, person.getUserName());
            }
        }
        String assignee = delegateTask.getAssignee();
        if (!StringUtils.isEmpty(assignee)) {
            Person person = organisePersonService.getPersonById(assignee);
            if (person != null) {
                //绑定任务接收人名称
                delegateTask.setVariableLocal(ProcessConstants.ASSIGNEE_NAME_KEY, person.getUserName());
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
