package com.dr.process.camunda.command.process.definition;

import com.dr.framework.core.process.service.ProcessService;
import com.dr.process.camunda.command.process.AbstractProcessDefinitionCmd;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;
import org.camunda.bpm.engine.repository.ProcessDefinitionQuery;
import org.springframework.util.StringUtils;

/**
 * @author dr
 */
public abstract class AbstractGetProcessQueryCmd extends AbstractProcessDefinitionCmd {
    private com.dr.framework.core.process.query.ProcessDefinitionQuery query;

    public AbstractGetProcessQueryCmd(com.dr.framework.core.process.query.ProcessDefinitionQuery query) {
        super(query == null ? ProcessService.DEFAULT_WITH_PROPERTIES : query.isWithProperty());
        this.query = query;
    }

    protected ProcessDefinitionQuery convertQuery(CommandContext commandContext) {
        return convertQuery(this.query, commandContext);
    }

    protected ProcessDefinitionQuery convertQuery(com.dr.framework.core.process.query.ProcessDefinitionQuery query, CommandContext commandContext) {
        ProcessDefinitionQuery pq = commandContext.getProcessEngineConfiguration()
                .getRepositoryService()
                .createProcessDefinitionQuery()
                //排序
                .orderByProcessDefinitionVersion()
                .desc()
                .orderByDeploymentTime()
                .desc();
        if (query != null) {
            if (query.isUseLatestVersion()) {
                pq.latestVersion();
            }
            if (!StringUtils.isEmpty(query.getName())) {
                pq.processDefinitionNameLike("%" + query.getName() + "%");
            }
            //TODO 根据类型查询
            if (!StringUtils.isEmpty(query.getType())) {
                //  pq.processDefinitionKeyLike(query.getType());
            }
        }
        pq.orderByProcessDefinitionKey().asc();
        return pq;
    }

}
