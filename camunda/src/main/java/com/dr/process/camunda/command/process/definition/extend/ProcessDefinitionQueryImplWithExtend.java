package com.dr.process.camunda.command.process.definition.extend;

import com.dr.process.camunda.annotations.SqlProxy;
import org.camunda.bpm.engine.impl.ProcessDefinitionQueryImpl;
import org.camunda.bpm.engine.impl.interceptor.CommandExecutor;

/**
 * 自定义流程定义查询
 * 连接了{@link ProcessDefinitionExtendEntity}扩展表的实现
 *
 * @author dr
 */
@SqlProxy(methodName = SqlProxy.METHOD_NAME_LIST, originalSql = "selectProcessDefinitionsByQueryCriteria", proxySql = "selectProcessDefinitionsByQueryCriteriaFix")
@SqlProxy(methodName = SqlProxy.METHOD_NAME_ONE, originalSql = "selectProcessDefinitionCountByQueryCriteria", proxySql = "selectProcessDefinitionCountByQueryCriteriaFix")
public class ProcessDefinitionQueryImplWithExtend extends ProcessDefinitionQueryImpl {
    //流程定义类型模糊查询
    private String processTypeLike;

    public ProcessDefinitionQueryImplWithExtend(CommandExecutor commandExecutor) {
        super(commandExecutor);
    }

    public String getProcessTypeLike() {
        return processTypeLike;
    }

    public void setProcessTypeLike(String processTypeLike) {
        this.processTypeLike = processTypeLike;
    }
}
