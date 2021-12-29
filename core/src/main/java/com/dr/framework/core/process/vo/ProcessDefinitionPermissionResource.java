package com.dr.framework.core.process.vo;

import com.dr.framework.core.process.bo.ProcessDefinition;

/**
 * 流程类型的权限资源
 * 流程定义可能关联数据太多，做个包装类，用来给平台权限模块使用
 *
 * @author dr
 */
public class ProcessDefinitionPermissionResource extends AbstractProcessPermissionResource {
    private transient final ProcessDefinition processDefinition;

    public ProcessDefinitionPermissionResource(ProcessDefinition processDefinition) {
        this.processDefinition = processDefinition;
    }

    @Override
    public String getCode() {
        return processDefinition.getId();
    }

    @Override
    public String getName() {
        return processDefinition.getName() + "(" + processDefinition.getVersion() + ")";
    }

    @Override
    public String getDescription() {
        return processDefinition.getDescription();
    }

    @Override
    public String getParentId() {
        return processDefinition.getType();
    }

    @Override
    public Integer getOrder() {
        return processDefinition.getVersion();
    }

    @Override
    public String getId() {
        return processDefinition.getId();
    }
}
