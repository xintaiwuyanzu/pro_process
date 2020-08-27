package com.dr.process.camunda.command.process;

import com.dr.framework.core.process.bo.ProcessObject;
import com.dr.framework.core.process.query.ProcessQuery;
import com.dr.process.camunda.query.CustomHistoricProcessInstanceQuery;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.history.HistoricProcessInstanceQuery;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;
import org.springframework.util.StringUtils;

/**
 * @author dr
 */
public class AbstractGetProcessObjectHistoryCmd {
    private ProcessQuery query;

    public AbstractGetProcessObjectHistoryCmd(ProcessQuery query) {
        this.query = query;
    }

    protected HistoricProcessInstanceQuery convert(CommandContext commandContext) {
        CustomHistoricProcessInstanceQuery hq = new CustomHistoricProcessInstanceQuery(commandContext.getProcessEngineConfiguration().getCommandExecutorTxRequired());
        if (query != null) {
            if (!StringUtils.isEmpty(query.getName())) {
                hq.processDefinitionNameLike(query.getName());
            }
            if (!StringUtils.isEmpty(query.getDescription())) {
            }
            if (!StringUtils.isEmpty(query.getType())) {
                hq.setProcessDefKeyLike(query.getType());
            }
            if (!StringUtils.isEmpty(query.getCreatePerson())) {
                hq.startedBy(query.getCreatePerson());
            }
        }
        return hq.finished();
    }

    protected ProcessObject convert(HistoricProcessInstance historicProcessInstance, CommandContext commandContext) {
        return commandContext.getProcessEngineConfiguration().getCommandExecutorTxRequired().execute(new ConvertProcessInstanceCmd(historicProcessInstance.getId()));
    }
}
