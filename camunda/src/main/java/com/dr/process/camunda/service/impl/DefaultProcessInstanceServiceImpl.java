package com.dr.process.camunda.service.impl;

import com.dr.framework.common.page.Page;
import com.dr.framework.core.process.bo.ProcessInstance;
import com.dr.framework.core.process.query.ProcessQuery;
import com.dr.framework.core.process.service.ProcessInstanceService;
import com.dr.process.camunda.command.process.instance.GetProcessInstanceListCmd;
import com.dr.process.camunda.command.process.instance.GetProcessInstancePageCmd;
import com.dr.process.camunda.command.process.history.GetProcessObjectHistoryListCmd;
import com.dr.process.camunda.command.process.history.GetProcessObjectHistoryPageCmd;
import org.camunda.bpm.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 默认流程实例实现类
 *
 * @author dr
 */
@Service
public class DefaultProcessInstanceServiceImpl extends BaseProcessServiceImpl implements ProcessInstanceService {
    @Autowired
    private RuntimeService runtimeService;

    @Override
    public List<ProcessInstance> processInstanceList(ProcessQuery query) {
        return getCommandExecutor().execute(new GetProcessInstanceListCmd(query));
    }

    @Override
    public Page<ProcessInstance> processInstancePage(ProcessQuery query, int start, int end) {
        return getCommandExecutor().execute(new GetProcessInstancePageCmd(query, start, end));
    }

    @Override
    public List<ProcessInstance> processInstanceHistoryList(ProcessQuery query) {
        return getCommandExecutor().execute(new GetProcessObjectHistoryListCmd(query));
    }

    @Override
    public Page<ProcessInstance> processInstanceHistoryPage(ProcessQuery query, int start, int end) {
        return getCommandExecutor().execute(new GetProcessObjectHistoryPageCmd(query, start, end));
    }


    @Override
    public void deleteProcessInstance(String processInstance, String deleteReason) {
        Assert.isTrue(!StringUtils.isEmpty(processInstance), "流程实例不能为空！");
        getRuntimeService().deleteProcessInstance(processInstance, deleteReason);
    }

    @Override
    public RuntimeService getRuntimeService() {
        return runtimeService;
    }
}
