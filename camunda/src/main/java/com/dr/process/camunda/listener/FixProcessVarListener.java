package com.dr.process.camunda.listener;

import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.organise.service.OrganisePersonService;
import com.dr.framework.core.process.service.ProcessConstants;
import com.dr.framework.core.security.SecurityHolder;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.engine.impl.util.ClockUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import static com.dr.process.camunda.listener.FixProcessVarListener.BEAN_NAME;

/**
 * 用来补全流程变量
 *
 * @author dr
 */
@Component(BEAN_NAME)
public class FixProcessVarListener implements ExecutionListener {
    public static final String BEAN_NAME = "FixProcessVarListener";
    private OrganisePersonService organisePersonService;

    public FixProcessVarListener(OrganisePersonService organisePersonService) {
        this.organisePersonService = organisePersonService;
    }

    @Override
    public void notify(DelegateExecution execution) {
        switch (execution.getEventName()) {
            case EVENTNAME_START:
                bindStartVar(execution);
                break;
            case EVENTNAME_END:
                bindEndVar(execution);
                break;
        }
    }

    /**
     * 流程办结时绑定流程变量
     *
     * @param execution
     */
    private void bindEndVar(DelegateExecution execution) {
        SecurityHolder securityHolder = SecurityHolder.get();
        //绑定当前登录人为办结人
        if (securityHolder != null && securityHolder.currentPerson() != null) {
            Person person = securityHolder.currentPerson();

            execution.setVariable(ProcessConstants.PROCESS_END_PERSON_KEY, person.getId());
            execution.setVariable(ProcessConstants.PROCESS_END_NAME_KEY, person.getUserName());
        }
    }

    /**
     * 绑定创建时变量
     *
     * @param execution
     */
    private void bindStartVar(DelegateExecution execution) {
        //创建人Id
        String createPerson = (String) execution.getVariable(ProcessConstants.PROCESS_CREATE_PERSON_KEY);
        if (!StringUtils.isEmpty(createPerson)) {
            //流程创建人名称
            Person person = organisePersonService.getPersonById(createPerson);
            execution.setVariable(ProcessConstants.PROCESS_CREATE_NAME_KEY, person.getUserName());
        }
        //流程创建时间
        execution.setVariable(ProcessConstants.PROCESS_CREATE_DATE_KEY, ClockUtil.getCurrentTime().getTime());
    }


}
