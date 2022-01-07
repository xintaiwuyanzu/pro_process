package com.dr.process.camunda.command.process.instance;

import com.dr.framework.core.process.bo.ProcessDefinition;
import com.dr.framework.core.process.bo.ProcessInstance;
import com.dr.process.camunda.command.process.definition.AbstractProcessDefinitionCmd;
import com.dr.process.camunda.command.process.definition.GetProcessDefinitionByIdCmd;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.history.HistoricVariableInstance;
import org.camunda.bpm.engine.impl.interceptor.Command;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;

import java.util.Map;
import java.util.stream.Collectors;

import static com.dr.framework.core.process.service.ProcessConstants.PROCESS_CREATE_NAME_KEY;
import static com.dr.framework.core.process.service.ProcessConstants.PROCESS_TITLE_KEY;


/**
 * 根据流转实例Id转换成自定义流程实例对象
 * <p>
 * TODO 带有额外属性查询参数
 *
 * @author dr
 */
public class ConvertProcessInstanceCmd implements Command<ProcessInstance> {
    /**
     * 流程实例Id
     */
    private String processInstanceId;
    /**
     * 带过来的额外参数
     */
    private Map<String, Object> valueMap;

    public ConvertProcessInstanceCmd(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public ConvertProcessInstanceCmd(String processInstanceId, Map<String, Object> valueMap) {
        this.processInstanceId = processInstanceId;
        this.valueMap = valueMap;
    }

    @Override
    public ProcessInstance execute(CommandContext commandContext) {
        if (processInstanceId == null) {
            return null;
        }
        HistoricProcessInstance his = commandContext.getProcessEngineConfiguration()
                .getHistoryService()
                .createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();
        ProcessInstance po = new ProcessInstance();
        po.setId(his.getId());
        po.setType(his.getBusinessKey());
        po.setName(his.getProcessDefinitionName());


        po.setVersion(his.getProcessDefinitionVersion());
        po.setCreateDate(his.getStartTime().getTime());
        if (his.getEndTime() != null) {
            po.setEndDate(his.getEndTime().getTime());
        }
        po.setCreatePerson(his.getStartUserId());
        po.setProcessDefineId(his.getProcessDefinitionId());
        po.setSuspend(po.isSuspend());
        Map<String, Object> variables = valueMap;
        if (variables == null) {
            variables = commandContext.getProcessEngineConfiguration().getHistoryService()
                    .createHistoricVariableInstanceQuery()
                    .processInstanceId(processInstanceId)
                    .activityInstanceIdIn(processInstanceId)
                    .list()
                    .stream()
                    .collect(Collectors.toMap(HistoricVariableInstance::getName, HistoricVariableInstance::getValue));
        }
        po.setDescription((String) variables.get(PROCESS_TITLE_KEY));
        po.setCreatePersonName((String) variables.get(PROCESS_CREATE_NAME_KEY));
        //TODO 过滤变量分很多种场景
        po.setVariables(AbstractProcessDefinitionCmd.filter(variables));
        //额外变量
        bindProperties(po, his, commandContext);
        return po;
    }

    /**
     * 绑定预定义参数
     *
     * @param po
     * @param his
     * @param commandContext
     */
    private void bindProperties(ProcessInstance po, HistoricProcessInstance his, CommandContext commandContext) {
        ProcessDefinition processDefinition = commandContext.getProcessEngineConfiguration().getCommandExecutorTxRequired()
                .execute(new GetProcessDefinitionByIdCmd(his.getProcessDefinitionId(), true));
        po.setProPerties(processDefinition.getProPerties());
    }
}
