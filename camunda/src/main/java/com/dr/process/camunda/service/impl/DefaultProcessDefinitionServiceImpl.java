package com.dr.process.camunda.service.impl;

import com.dr.framework.common.page.Page;
import com.dr.framework.core.process.bo.ProcessDefinition;
import com.dr.framework.core.process.query.ProcessDefinitionQuery;
import com.dr.framework.core.process.service.ProcessDefinitionService;
import com.dr.process.camunda.command.process.definition.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 默认流程定义实现类
 *
 * @author dr
 */
@Service
public class DefaultProcessDefinitionServiceImpl extends BaseProcessServiceImpl implements ProcessDefinitionService {

    @Override
    @Transactional(readOnly = true)
    public ProcessDefinition getProcessDefinitionById(String id) {
        return getCommandExecutor().execute(new GetProcessDefinitionByIdCmd(id));
    }

    @Override
    @Transactional(readOnly = true)
    public ProcessDefinition getProcessDefinitionByKey(String key) {
        return getCommandExecutor().execute(new GetProcessDefinitionByKeyCmd(key));
    }

    @Override
    @Transactional(readOnly = true)
    public long countDefinition(ProcessDefinitionQuery query) {
        return getCommandExecutor().execute(new GetProcessDefinitionCountCmd(query));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProcessDefinition> processDefinitionList(ProcessDefinitionQuery processDefinitionQuery) {
        return getCommandExecutor().execute(new GetProcessDefinitionListCmd(processDefinitionQuery));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProcessDefinition> processDefinitionPage(ProcessDefinitionQuery processDefinitionQuery, int start, int end) {
        return getCommandExecutor().execute(new GetProcessDefinitionPageCmd(processDefinitionQuery, start, end));
    }
}
