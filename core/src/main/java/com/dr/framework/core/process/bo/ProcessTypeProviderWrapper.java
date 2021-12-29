package com.dr.framework.core.process.bo;

import com.dr.framework.core.process.service.ProcessTypeProvider;

/**
 * 流程类型封装类，用来封装对象给前台使用
 *
 * @author dr
 */
public class ProcessTypeProviderWrapper implements ProcessTypeProvider {
    /**
     * 原始对象不会被序列化
     */
    private transient ProcessTypeProvider rawProvider;

    public ProcessTypeProviderWrapper(ProcessTypeProvider rawProvider) {
        this.rawProvider = rawProvider;
    }

    @Override
    public String getName() {
        return rawProvider.getName();
    }

    @Override
    public String getType() {
        return rawProvider.getType();
    }

    @Override
    public String getDescription() {
        return rawProvider.getDescription();
    }

    @Override
    public int getOrder() {
        return rawProvider.getOrder();
    }

    @Override
    public String getRoleCode() {
        return rawProvider.getRoleCode();
    }
}
