package com.dr.process.camunda.parselistener;

import com.dr.framework.core.process.service.ProcessConstants;
import com.dr.process.camunda.listener.FixProcessVarListener;
import com.dr.process.camunda.listener.FixTaskVarListener;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.camunda.bpm.engine.impl.bpmn.listener.DelegateExpressionExecutionListener;
import org.camunda.bpm.engine.impl.bpmn.parser.AbstractBpmnParseListener;
import org.camunda.bpm.engine.impl.bpmn.parser.BpmnParse;
import org.camunda.bpm.engine.impl.el.Expression;
import org.camunda.bpm.engine.impl.el.ExpressionManager;
import org.camunda.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.camunda.bpm.engine.impl.pvm.process.ActivityImpl;
import org.camunda.bpm.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.camunda.bpm.engine.impl.pvm.process.ScopeImpl;
import org.camunda.bpm.engine.impl.task.TaskDefinition;
import org.camunda.bpm.engine.impl.task.listener.DelegateExpressionTaskListener;
import org.camunda.bpm.engine.impl.util.xml.Element;
import org.springframework.util.StringUtils;

/**
 * @author dr
 */
public class CustomerEventListenerParser extends AbstractBpmnParseListener {
    protected Expression taskListenerExpression;
    protected Expression processListenerExpression;
    protected Expression assigneeExpression;
    protected Expression descriptionExpression;

    public CustomerEventListenerParser(ExpressionManager expressionManager) {
        taskListenerExpression = expressionManager.createExpression("${" + FixTaskVarListener.BEAN_NAME + "}");
        processListenerExpression = expressionManager.createExpression("${" + FixProcessVarListener.BEAN_NAME + "}");
        assigneeExpression = expressionManager.createExpression("${assignee}");
        descriptionExpression = expressionManager.createExpression("${$description}");
    }

    @Override
    public void parseProcess(Element processElement, ProcessDefinitionEntity processDefinition) {
        super.parseProcess(processElement, processDefinition);
        //流程事件监听器
        DelegateExpressionExecutionListener delegateListener = new DelegateExpressionExecutionListener(processListenerExpression, null);
        processDefinition.addListener(ExecutionListener.EVENTNAME_START, delegateListener);
        processDefinition.addListener(ExecutionListener.EVENTNAME_END, delegateListener);
    }

    @Override
    public void parseStartEvent(Element startEventElement, ScopeImpl scope, ActivityImpl startEventActivity) {
        super.parseStartEvent(startEventElement, scope, startEventActivity);
        ProcessDefinitionImpl processDefinition = startEventActivity.getProcessDefinition();
        String init = (String) processDefinition.getProperty(BpmnParse.PROPERTYNAME_INITIATOR_VARIABLE_NAME);
        if (StringUtils.isEmpty(init)) {
            processDefinition.setProperty(BpmnParse.PROPERTYNAME_INITIATOR_VARIABLE_NAME, ProcessConstants.PROCESS_CREATE_PERSON_KEY);
        }
    }

    @Override
    public void parseUserTask(Element userTaskElement, ScopeImpl scope, ActivityImpl activity) {
        super.parseUserTask(userTaskElement, scope, activity);
        UserTaskActivityBehavior activityBehavior = (UserTaskActivityBehavior) activity.getActivityBehavior();
        TaskDefinition taskDefinition = activityBehavior.getTaskDefinition();
        //手动设置接收人表达式
        if (taskDefinition.getAssigneeExpression() == null) {
            taskDefinition.setAssigneeExpression(assigneeExpression);
        }
        //手动设置任务描述表达式
        if (taskDefinition.getDescriptionExpression() == null) {
            taskDefinition.setDescriptionExpression(descriptionExpression);
        }
        TaskListener delegateListener = new DelegateExpressionTaskListener(taskListenerExpression, null);
        //添加用户任务监听
        //任务创建
        taskDefinition.addTaskListener(TaskListener.EVENTNAME_CREATE, delegateListener);
        //任务分配
        taskDefinition.addTaskListener(TaskListener.EVENTNAME_ASSIGNMENT, delegateListener);
        //任务完成
        taskDefinition.addTaskListener(TaskListener.EVENTNAME_COMPLETE, delegateListener);
    }
}
