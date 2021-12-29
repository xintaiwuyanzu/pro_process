package com.dr.framework.core.process.service;

import org.springframework.core.Ordered;

/**
 * 流程类型定义
 *
 * @author dr
 */
public interface ProcessTypeProvider extends Ordered {

    /**
     * 获取指定类型的流程对应的角色
     *
     * @return
     */
    default String getRoleCode() {
        return null;
    }

    /**
     * 流程启动前回调业务方法
     *
     * @param context
     */
    default void onBeforeStartProcess(ProcessContext context) {

    }

    /**
     * 流程启动后回调业务方法
     *
     * @param context
     */
    default void onAfterStartProcess(ProcessContext context) {

    }

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
