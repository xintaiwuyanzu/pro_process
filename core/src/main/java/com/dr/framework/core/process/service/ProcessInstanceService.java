package com.dr.framework.core.process.service;

import com.dr.framework.common.page.Page;
import com.dr.framework.core.process.bo.ProcessInstance;
import com.dr.framework.core.process.query.ProcessQuery;

import java.util.List;

/**
 * 流程实例service
 *
 * @author dr
 */
public interface ProcessInstanceService {
    /**
     * 查询流程实例
     *
     * @param query
     * @return
     */
    List<ProcessInstance> processInstanceList(ProcessQuery query);

    /**
     * 查询流程实例分页
     *
     * @param query
     * @param start
     * @param end
     * @return
     */
    Page<ProcessInstance> processInstancePage(ProcessQuery query, int start, int end);

    /**
     * 查询流程实例历史
     *
     * @param query
     * @return
     */
    List<ProcessInstance> processInstanceHistoryList(ProcessQuery query);

    /**
     * 查询流程实例历史分页
     *
     * @param query
     * @param start
     * @param end
     * @return
     */
    Page<ProcessInstance> processInstanceHistoryPage(ProcessQuery query, int start, int end);


    /**
     * @param processInstance
     * @param deleteReason
     */
    void deleteProcessInstance(String processInstance, String deleteReason);
}
