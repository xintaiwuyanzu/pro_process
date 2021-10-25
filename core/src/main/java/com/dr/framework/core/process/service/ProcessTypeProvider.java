package com.dr.framework.core.process.service;

import org.springframework.core.Ordered;

/**
 * 流程类型定义
 *
 * @author dr
 */
public interface ProcessTypeProvider extends Ordered {
    /**
     * 流程类型
     *
     * @return
     */
    String getType();

    /**
     * 流程类型名称
     *
     * @return
     */
    String getName();

    /**
     * 流程类型描述
     *
     * @return
     */
    default String getDescription() {
        return "";
    }

    @Override
    default int getOrder() {
        return 0;
    }
}
