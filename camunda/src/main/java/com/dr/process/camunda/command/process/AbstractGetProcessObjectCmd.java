package com.dr.process.camunda.command.process;

import com.dr.framework.core.process.bo.ProcessObject;
import com.dr.framework.core.process.query.ProcessQuery;
import com.dr.framework.core.process.service.ProcessService;
import com.dr.process.camunda.query.CustomHistoricProcessInstanceQuery;
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
public class AbstractGetProcessObjectCmd {
    private ProcessQuery query;

    public AbstractGetProcessObjectCmd(ProcessQuery query) {
        this.query = query;
    }

    protected HistoricProcessInstanceQuery convert(CommandContext commandContext) {
        CustomHistoricProcessInstanceQuery pq = new CustomHistoricProcessInstanceQuery(commandContext.getProcessEngineConfiguration().getCommandExecutorTxRequired());
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

    protected ProcessObject convert(HistoricProcessInstance instance, CommandContext commandContext) {
        return commandContext.getProcessEngineConfiguration()
                .getCommandExecutorTxRequired()
                .execute(new ConvertProcessInstanceCmd(instance.getId()));
    }

}
