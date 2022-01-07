package com.dr.process.camunda.command;

import com.dr.framework.core.process.query.ProcessInstanceQuery;
import com.dr.framework.core.process.service.ProcessConstants;
import org.camunda.bpm.engine.history.HistoricProcessInstanceQuery;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;
import org.springframework.util.StringUtils;

/**
 * 查询方法转换工具类
 * 转换查询方法工具类，所有相关的转换都放在一起，省的别的地方漏了
 *
 * @author dr
 */
public class QueryUtils {
    /**
     * 查询流程历史实例
     *
     * @param commandContext
     * @param query
     * @return
     */
    public static HistoricProcessInstanceQuery processHistoryQuery(CommandContext commandContext, ProcessInstanceQuery query) {
        HistoricProcessInstanceQuery hq = commandContext.getProcessEngineConfiguration().getHistoryService().createHistoricProcessInstanceQuery();
        if (query != null) {
            if (!StringUtils.isEmpty(query.getName())) {
                //根据标题查询流程实例
                hq.variableValueLike(ProcessConstants.PROCESS_TITLE_KEY, "%" + query.getName() + "%S");
            }
            if (!StringUtils.isEmpty(query.getDescription())) {
                //根据描述查询流程实例
                hq.variableValueLike(ProcessConstants.PROCESS_DESCRIPTION_KEY, query.getDescription());
            }
            if (!StringUtils.isEmpty(query.getType())) {
                //根据流程类型查询流程实例
                hq.variableValueLike(ProcessConstants.PROCESS_TYPE_KEY, query.getType());
            }
            if (!StringUtils.isEmpty(query.getCreatePerson())) {
                //流程启动人
                hq.startedBy(query.getCreatePerson());
            }
        }
        return hq.finished();
    }

}
