package com.dr.process.camunda.command.process.definition;

import com.dr.framework.core.process.service.ProcessConstants;
import com.dr.process.camunda.annotations.SqlProxy;
import com.dr.process.camunda.command.process.definition.extend.ProcessDefinitionExtendEntity;
import org.camunda.bpm.engine.impl.ProcessDefinitionQueryImpl;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;
import org.camunda.bpm.engine.impl.interceptor.CommandExecutor;
import org.camunda.bpm.engine.repository.ProcessDefinitionQuery;
import org.springframework.util.StringUtils;

/**
 * 根据查询条件查询列表或者分页流程定义信息
 *
 * @author dr
 */
public abstract class AbstractGetProcessQueryCmd extends AbstractProcessDefinitionCmd {
    /**
     * 流程定义查询实体
     */
    private com.dr.framework.core.process.query.ProcessDefinitionQuery query;

    public AbstractGetProcessQueryCmd(com.dr.framework.core.process.query.ProcessDefinitionQuery query) {
        super(query == null ? ProcessConstants.DEFAULT_WITH_PROPERTIES : query.isWithProperty());
        this.query = query;
    }

    protected ProcessDefinitionQuery convertQuery(CommandContext commandContext) {
        return convertQuery(this.query, commandContext);
    }

    /**
     * 转换成camunda内置的查询对象
     *
     * @param query
     * @param commandContext
     * @return
     */
    protected ProcessDefinitionQuery convertQuery(com.dr.framework.core.process.query.ProcessDefinitionQuery query, CommandContext commandContext) {
        //使用自定义扩展查询方法
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

    /**
     * 自定义流程定义查询
     * 连接了{@link ProcessDefinitionExtendEntity}扩展表的实现
     *
     * @author dr
     */
    @SqlProxy(methodName = SqlProxy.METHOD_NAME_LIST, originalSql = "selectProcessDefinitionsByQueryCriteria", proxySql = "selectProcessDefinitionsByQueryCriteriaFix")
    @SqlProxy(methodName = SqlProxy.METHOD_NAME_ONE, originalSql = "selectProcessDefinitionCountByQueryCriteria", proxySql = "selectProcessDefinitionCountByQueryCriteriaFix")
    public static class ProcessDefinitionQueryImplWithExtend extends ProcessDefinitionQueryImpl {
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

}
