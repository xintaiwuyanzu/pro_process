package com.dr.process.camunda.command.process.instance;

import com.dr.framework.core.process.bo.ProcessInstance;
import com.dr.framework.core.process.query.ProcessInstanceQuery;
import com.dr.process.camunda.command.QueryUtils;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.history.HistoricProcessInstanceQuery;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;

/**
 * 流程实例没有
 * 创建人、创建时间等业务相关的信息，
 * 全是逻辑数据，所以需要从历史数据中查询
 *
 * @author dr
 */
public class AbstractGetProcessInstanceCmd {
    private ProcessInstanceQuery query;

    public AbstractGetProcessInstanceCmd(ProcessInstanceQuery query) {
        this.query = query;
    }

    protected HistoricProcessInstanceQuery convert(CommandContext commandContext) {
        return QueryUtils.processHistoryQuery(commandContext, query)
                //根据启动时间倒序排序
                .orderByProcessInstanceStartTime().desc();
    }

    /**
     * 根据历史数据查询id，再根据id查询实例
     *
     * @param instance
     * @param commandContext
     * @return
     */
    protected ProcessInstance convert(HistoricProcessInstance instance, CommandContext commandContext) {
        return commandContext.getProcessEngineConfiguration().getCommandExecutorTxRequired().execute(new ConvertProcessInstanceCmd(instance.getId()));
    }


}
