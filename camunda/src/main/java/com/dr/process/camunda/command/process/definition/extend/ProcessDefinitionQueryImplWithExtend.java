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
@SqlProxy(methodName = "selectList", originalSql = "selectProcessDefinitionsByQueryCriteria", proxySql = "selectProcessDefinitionsByQueryCriteria")
public class ProcessDefinitionQueryImplWithExtend extends ProcessDefinitionQueryImpl {

    public ProcessDefinitionQueryImplWithExtend(CommandExecutor commandExecutor) {
        super(commandExecutor);
    }
}
