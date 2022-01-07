package com.dr.process.camunda.command.process.history;

import com.dr.framework.core.process.bo.ProcessInstance;
import com.dr.framework.core.process.query.ProcessInstanceQuery;
import com.dr.process.camunda.command.QueryUtils;
import com.dr.process.camunda.command.process.instance.ConvertProcessInstanceCmd;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.history.HistoricProcessInstanceQuery;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;

/**
 * 根据条件查询流转历史实例抽象父类
 * TODO 统计方法
 *
 * @author dr
 */
public class AbstractGetProcessHistoryCmd {
    private ProcessInstanceQuery query;

    public AbstractGetProcessHistoryCmd(ProcessInstanceQuery query) {
        this.query = query;
    }

    /**
     * 转换查询方法
     *
     * @param commandContext
     * @return
     */
    protected HistoricProcessInstanceQuery convert(CommandContext commandContext) {
        return QueryUtils.processHistoryQuery(commandContext, query)
                //根据办结时间倒叙排序
                .orderByProcessInstanceEndTime().desc();
    }

    /**
     * 转换流转历史为流程对外的流转对象
     *
     * @param historicProcessInstance
     * @param commandContext
     * @return
     */
    protected ProcessInstance convert(HistoricProcessInstance historicProcessInstance, CommandContext commandContext) {
        return commandContext.getProcessEngineConfiguration().getCommandExecutorTxRequired().execute(new ConvertProcessInstanceCmd(historicProcessInstance.getId()));
    }
}
