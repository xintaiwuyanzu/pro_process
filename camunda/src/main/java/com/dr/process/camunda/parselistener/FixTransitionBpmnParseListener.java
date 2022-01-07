package com.dr.process.camunda.parselistener;

import org.camunda.bpm.engine.ActivityTypes;
import org.camunda.bpm.engine.impl.Condition;
import org.camunda.bpm.engine.impl.bpmn.helper.BpmnProperties;
import org.camunda.bpm.engine.impl.bpmn.parser.AbstractBpmnParseListener;
import org.camunda.bpm.engine.impl.bpmn.parser.BpmnParse;
import org.camunda.bpm.engine.impl.core.model.Properties;
import org.camunda.bpm.engine.impl.el.ExpressionManager;
import org.camunda.bpm.engine.impl.el.UelExpressionCondition;
import org.camunda.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.camunda.bpm.engine.impl.pvm.process.ActivityImpl;
import org.camunda.bpm.engine.impl.pvm.process.TransitionImpl;
import org.camunda.bpm.engine.impl.util.xml.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 在解析bpmn流程文件完成的时候
 * 把所有节点都连接上，用来实现任意环节的跳转
 *
 * @author dr
 */
public class FixTransitionBpmnParseListener extends AbstractBpmnParseListener {
    static final Logger LOGGER = LoggerFactory.getLogger(FixTransitionBpmnParseListener.class);
    /**
     * 自己添加连接线条件key
     */
    public static final String SELF_TRANSITION_FIX_CONDITION_KEY = "nextActivityId";
    protected ExpressionManager expressionManager;

    public FixTransitionBpmnParseListener(ExpressionManager expressionManager) {
        this.expressionManager = expressionManager;
    }

    @Override
    public void parseProcess(Element processElement, ProcessDefinitionEntity processDefinition) {
        super.parseProcess(processElement, processDefinition);
        //获取排除了开始节点的所有节点
        List<ActivityImpl> activitiesWithOutStart = filterStartActivities(processDefinition);
        for (ActivityImpl task : activitiesWithOutStart) {
            if (isEndTask(task)) {
                continue;
            }
            //把所有节点都相互连接上(包括自己连自己)
            fixTransition(task, activitiesWithOutStart);
        }
    }


    /**
     * 链接所有的任务节点
     *
     * @param task
     * @param endActivities
     */
    protected void fixTransition(ActivityImpl task, List<ActivityImpl> endActivities) {
        //先算出来所有已经连接的节点了
        Set<String> outIds = task.getOutgoingTransitions().stream().map(t -> t.getDestination().getId()).collect(Collectors.toSet());
        //在计算出来需要连接的节点
        endActivities.stream().filter(act -> !outIds.contains(act.getActivityId())).forEach(act -> doFix(task, act));
    }


    private void doFix(ActivityImpl start, ActivityImpl destination) {
        //是不是不用设置Id了？
        String transId = UUID.randomUUID().toString();
        TransitionImpl fixTransition = start.createOutgoingTransition(transId);
        fixTransition.setDestination(destination);
        fixTransition.setProperty("name", "fix");
        fixTransition.setProperty("fix", true);

        StringBuilder sb = new StringBuilder("${").append(SELF_TRANSITION_FIX_CONDITION_KEY).append("=='").append(destination.getActivityId()).append("'}");
        Condition condition = new UelExpressionCondition(expressionManager.createExpression(sb.toString()));
        fixTransition.setProperty(BpmnParse.PROPERTYNAME_CONDITION_TEXT, sb.toString());
        fixTransition.setProperty(BpmnParse.PROPERTYNAME_CONDITION, condition);
    }

    /**
     * 获取结束的节点
     *
     * @param processDefinition
     * @return
     */
    protected List<ActivityImpl> filterStartActivities(ProcessDefinitionEntity processDefinition) {
        return processDefinition.getActivities().stream().filter(act -> {
            Properties properties = act.getProperties();
            if (properties.contains(BpmnProperties.TYPE)) {
                return !START_EVENT_SETS.contains(properties.get(BpmnProperties.TYPE));
            }
            return true;
        }).collect(Collectors.toList());
    }


    /**
     * 判断是否结束节点
     *
     * @param act
     * @return
     */
    private boolean isEndTask(ActivityImpl act) {
        Properties properties = act.getProperties();
        if (properties.contains(BpmnProperties.TYPE)) {
            return END_EVENT_SETS.contains(properties.get(BpmnProperties.TYPE));
        }
        return false;
    }

    static final Set<String> START_EVENT_SETS = Stream.of(ActivityTypes.START_EVENT, ActivityTypes.START_EVENT_TIMER, ActivityTypes.START_EVENT_MESSAGE, ActivityTypes.START_EVENT_SIGNAL, ActivityTypes.START_EVENT_ESCALATION, ActivityTypes.START_EVENT_COMPENSATION, ActivityTypes.START_EVENT_ERROR, ActivityTypes.START_EVENT_CONDITIONAL).collect(Collectors.toSet());
    /**
     * 结束类型的节点
     */
    static final Set<String> END_EVENT_SETS = Stream.of(ActivityTypes.END_EVENT_ERROR, ActivityTypes.END_EVENT_CANCEL, ActivityTypes.END_EVENT_TERMINATE, ActivityTypes.END_EVENT_MESSAGE, ActivityTypes.END_EVENT_SIGNAL, ActivityTypes.END_EVENT_COMPENSATION, ActivityTypes.END_EVENT_ESCALATION, ActivityTypes.END_EVENT_NONE).collect(Collectors.toSet());
}
