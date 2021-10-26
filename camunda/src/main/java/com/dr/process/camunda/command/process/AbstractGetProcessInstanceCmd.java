package com.dr.process.camunda.command.process;

import com.dr.framework.core.process.bo.ProcessInstance;
import com.dr.framework.core.process.query.ProcessQuery;
import com.dr.framework.core.process.service.ProcessService;
import com.dr.process.camunda.annotations.SqlProxy;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.history.HistoricProcessInstanceQuery;
import org.camunda.bpm.engine.impl.HistoricProcessInstanceQueryImpl;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;
import org.camunda.bpm.engine.impl.interceptor.CommandExecutor;
import org.springframework.util.StringUtils;

/**
 * 流程实例没有
 * 创建人、创建时间等业务相关的信息，
 * 全是逻辑数据，所以需要从历史数据中查询
 *
 * @author dr
 */
public class AbstractGetProcessInstanceCmd {
    private ProcessQuery query;

    public AbstractGetProcessInstanceCmd(ProcessQuery query) {
        this.query = query;
    }

    protected HistoricProcessInstanceQuery convert(CommandContext commandContext) {
        HistoricProcessInstanceQueryImplWithExtend pq = new HistoricProcessInstanceQueryImplWithExtend(commandContext.getProcessEngineConfiguration().getCommandExecutorTxRequired());
        if (query != null) {
            if (!StringUtils.isEmpty(query.getDescription())) {
                pq.variableValueLike(ProcessService.TITLE_KEY, query.getDescription());
            }
            pq.startedBy(query.getCreatePerson());
            if (!StringUtils.isEmpty(query.getName())) {
                pq.processDefinitionNameLike(query.getName());
            }
            if (!StringUtils.isEmpty(query.getType())) {
                pq.setProcessDefKeyLike(query.getType());
            }
        }
        return pq.active()
                .orderByProcessInstanceStartTime()
                .asc();
    }

    protected ProcessInstance convert(HistoricProcessInstance instance, CommandContext commandContext) {
        return commandContext.getProcessEngineConfiguration()
                .getCommandExecutorTxRequired()
                .execute(new ConvertProcessInstanceCmd(instance.getId()));
    }

    @SqlProxy(methodName = "selectList", originalSql = "selectHistoricProcessInstancesByQueryCriteria", proxySql = "selectHistoricProcessInstancesByQueryCriteriaCustom")
    @SqlProxy(methodName = "selectOne", originalSql = "selectHistoricProcessInstanceCountByQueryCriteria", proxySql = "selectHistoricProcessInstanceCountByQueryCriteriaCustom")
    public static class HistoricProcessInstanceQueryImplWithExtend extends HistoricProcessInstanceQueryImpl {
        //根据流程定义key模糊查询
        private String processDefKeyLike;

        public HistoricProcessInstanceQueryImplWithExtend(CommandExecutor commandExecutor) {
            super(commandExecutor);
        }

        public String getProcessDefKeyLike() {
            return processDefKeyLike;
        }

        public void setProcessDefKeyLike(String processDefKeyLike) {
            this.processDefKeyLike = processDefKeyLike;
        }
    }

}
