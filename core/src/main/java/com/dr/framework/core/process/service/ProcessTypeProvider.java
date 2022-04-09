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
     * 环节完成前回调
     *
     * @param context
     */
    default void onBeforeCompleteTask(TaskContext context) {

    }

    /**
     * 环节完成后回调
     *
     * @param context
     */
    default void onAfterCompleteTask(TaskContext context) {

    }

    /**
     * 流程办结前回调
     *
     * @param context
     */
    default void onBeforeEndProcess(TaskContext context) {

    }

    /**
     * 环节办结前回调
     *
     * @param context
     */
    default void onAfterEndProcess(TaskContext context) {

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

    /**
     * 获取流程详情地址，默认是空，可以被拦截修改，如果没有设置就使用这个
     *
     * @param context
     * @return
     */
    default String getFormUrl(ProcessContext context) {
        return "";
    }

    /**
     * 设置默认排序
     *
     * @return
     */
    @Override
    default int getOrder() {
        return 0;
    }

}
