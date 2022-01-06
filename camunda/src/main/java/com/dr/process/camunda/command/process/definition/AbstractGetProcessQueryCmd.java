package com.dr.process.camunda.command.process.definition;

import com.dr.framework.core.process.service.ProcessConstants;
import com.dr.process.camunda.command.process.definition.extend.ProcessDefinitionQueryImplWithExtend;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;
import org.camunda.bpm.engine.repository.ProcessDefinitionQuery;
import org.springframework.util.StringUtils;

/**
 * @author dr
 */
public abstract class AbstractGetProcessQueryCmd extends AbstractProcessDefinitionCmd {
    private com.dr.framework.core.process.query.ProcessDefinitionQuery query;

    public AbstractGetProcessQueryCmd(com.dr.framework.core.process.query.ProcessDefinitionQuery query) {
        super(query == null ? ProcessConstants.DEFAULT_WITH_PROPERTIES : query.isWithProperty());
        this.query = query;
    }

    protected ProcessDefinitionQuery convertQuery(CommandContext commandContext) {
        return convertQuery(this.query, commandContext);
    }

    protected ProcessDefinitionQuery convertQuery(com.dr.framework.core.process.query.ProcessDefinitionQuery query, CommandContext commandContext) {
        ProcessDefinitionQueryImplWithExtend pq = new ProcessDefinitionQueryImplWithExtend(commandContext.getProcessEngineConfiguration().getCommandExecutorTxRequired());
        if (query != null) {
            if (query.isUseLatestVersion()) {
                pq.latestVersion();
            }
            if (!StringUtils.isEmpty(query.getName())) {
                pq.processDefinitionNameLike("%" + query.getName() + "%");
            }
            if (!StringUtils.isEmpty(query.getType())) {
                pq.processDefinitionKeyLike(query.getType());
            }
        }
        pq
                //根据版本倒叙排序
                .orderByProcessDefinitionVersion().desc()
                //根据部署时间倒叙排序
                .orderByDeploymentTime().desc()
                //根据流程定义编码排序
                .orderByProcessDefinitionKey().asc();
        return pq;
    }

}
