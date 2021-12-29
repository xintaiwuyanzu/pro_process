package com.dr.framework.core.process.vo;

import com.dr.framework.core.process.service.ProcessTypeProvider;
import com.dr.framework.core.util.Constants;

/**
 * 流程类型权限类型
 *
 * @author dr
 */
public class ProcessTypePermissionResource extends AbstractProcessPermissionResource {
    private final transient ProcessTypeProvider typeProvider;

    public ProcessTypePermissionResource(ProcessTypeProvider typeProvider) {
        this.typeProvider = typeProvider;
    }

    @Override
    public String getCode() {
        return typeProvider.getType();
    }

    @Override
    public String getDescription() {
        return typeProvider.getDescription();
    }

    @Override
    public String getParentId() {
        return Constants.DEFAULT;
    }

    @Override
    public Integer getOrder() {
        return typeProvider.getOrder();
    }

    @Override
    public String getName() {
        return typeProvider.getName();
    }

    @Override
    public String getId() {
        return typeProvider.getType();
    }
}
