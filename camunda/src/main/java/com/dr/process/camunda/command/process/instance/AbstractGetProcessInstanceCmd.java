package com.dr.process.camunda.command.process.instance;

import com.dr.framework.core.process.bo.ProcessInstance;
import com.dr.framework.core.process.query.ProcessQuery;
import com.dr.framework.core.process.service.ProcessConstants;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.history.HistoricProcessInstanceQuery;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;
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
        HistoricProcessInstanceQuery pq = commandContext.getProcessEngineConfiguration().getHistoryService().createHistoricProcessInstanceQuery();
        if (query != null) {
            if (!StringUtils.isEmpty(query.getDescription())) {
                pq.variableValueLike(ProcessConstants.PROCESS_TITLE_KEY, query.getDescription());
            }
            pq.startedBy(query.getCreatePerson());
            if (!StringUtils.isEmpty(query.getName())) {
                pq.processDefinitionNameLike(query.getName());
            }
            if (!StringUtils.isEmpty(query.getType())) {
                pq.variableValueLike(ProcessConstants.PROCESS_TYPE_KEY, query.getType());
            }
        }
        return pq.active().orderByProcessInstanceStartTime().asc();
    }

    protected ProcessInstance convert(HistoricProcessInstance instance, CommandContext commandContext) {
        return commandContext.getProcessEngineConfiguration().getCommandExecutorTxRequired().execute(new ConvertProcessInstanceCmd(instance.getId()));
    }


}