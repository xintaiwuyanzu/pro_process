package com.dr.process.camunda.listener;

import com.dr.framework.core.process.service.ProcessConstants;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.camunda.bpm.engine.impl.pvm.PvmActivity;
import org.camunda.bpm.engine.impl.pvm.delegate.ActivityExecution;
import org.camunda.bpm.engine.variable.value.StringValue;
import org.springframework.util.StringUtils;

/**
 * 自定义监听器，用来从变量中取意见数据，添加审批意见
 *
 * @author dr
 */
public class TaskCommentListener implements ExecutionListener {
    TaskService taskService;

    public TaskCommentListener(TaskService taskService) {
        this.taskService = taskService;
    }

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        if (execution instanceof ActivityExecution) {
            PvmActivity activity = ((ActivityExecution) execution).getActivity();
            if (activity.getActivityBehavior() instanceof UserTaskActivityBehavior) {
                StringValue comment = execution.getVariableTyped(ProcessConstants.VAR_COMMENT_KEY);
                if (comment == null) {
                    comment = execution.getVariableLocalTyped(ProcessConstants.VAR_COMMENT_KEY);
                }
                if (comment != null) {
                    if (StringUtils.hasText(comment.getValue())) {
                        //添加审核意见
                        taskService.createComment(execution.getActivityInstanceId(), execution.getProcessInstanceId(), comment.getValue());
                    }
                }
            }
        }
    }
}
