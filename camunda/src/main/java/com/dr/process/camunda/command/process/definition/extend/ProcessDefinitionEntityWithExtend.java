package com.dr.process.camunda.command.process.definition.extend;

import org.camunda.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;

/**
 * 带有扩展信息的流程定义实体
 *
 * @author dr
 */
public class ProcessDefinitionEntityWithExtend extends ProcessDefinitionEntity {
    private String processType;

    public String getProcessType() {
        return processType;
    }

    public void setProcessType(String processType) {
        this.processType = processType;
    }
}
