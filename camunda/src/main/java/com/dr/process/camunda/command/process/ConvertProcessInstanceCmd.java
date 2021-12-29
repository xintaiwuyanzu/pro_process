package com.dr.process.camunda.command.process;

import com.dr.framework.core.process.bo.ProcessInstance;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.history.HistoricVariableInstance;
import org.camunda.bpm.engine.impl.interceptor.Command;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;

import java.util.Map;
import java.util.stream.Collectors;

import static com.dr.framework.core.process.service.ProcessConstants.CREATE_NAME_KEY;
import static com.dr.framework.core.process.service.ProcessConstants.TITLE_KEY;


/**
 * @author dr
 */
public class ConvertProcessInstanceCmd implements Command<ProcessInstance> {
    private String processInstanceId;
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

        Map<String, Object> variables = valueMap;
        if (variables == null) {
           /* variables = commandContext.getProcessEngineConfiguration()
                    .getRuntimeService()
                    .getVariables(processInstanceId);*/
            variables = commandContext.getProcessEngineConfiguration().getHistoryService()
                    .createHistoricVariableInstanceQuery()
                    .processInstanceId(processInstanceId)
                    .activityInstanceIdIn(processInstanceId)
                    .list()
                    .stream()
                    .collect(Collectors.toMap(HistoricVariableInstance::getName, HistoricVariableInstance::getValue));
        }
        po.setVersion(his.getProcessDefinitionVersion());
        po.setCreateDate(his.getStartTime().getTime());
        if (his.getEndTime() != null) {
            po.setEndDate(his.getEndTime().getTime());
        }
        po.setCreatePerson(his.getStartUserId());
        po.setProcessDefineId(his.getProcessDefinitionId());
        po.setSuspend(po.isSuspend());

        if (variables != null) {
            po.setDescription((String) variables.get(TITLE_KEY));
            po.setCreatePersonName((String) variables.get(CREATE_NAME_KEY));
            po.setVariables(AbstractProcessDefinitionCmd.filter(variables));
        }
        return po;
    }
}
