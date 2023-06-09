package com.dr.framework.core.process.service;

import com.dr.framework.common.page.Page;
import com.dr.framework.core.process.bo.ProcessInstance;
import com.dr.framework.core.process.query.ProcessInstanceQuery;

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
    List<ProcessInstance> processInstanceList(ProcessInstanceQuery query);

    /**
     * 查询流程实例分页
     *
     * @param query
     * @param pageIndex
     * @param pageSize
     * @return
     */
    Page<ProcessInstance> processInstancePage(ProcessInstanceQuery query, int pageIndex, int pageSize);

    /**
     * 查询流程实例历史
     *
     * @param query
     * @return
     */
    List<ProcessInstance> processInstanceHistoryList(ProcessInstanceQuery query);

    /**
     * 查询流程实例历史分页
     *
     * @param query
     * @param pageIndex
     * @param pageSize
     * @return
     */
    Page<ProcessInstance> processInstanceHistoryPage(ProcessInstanceQuery query, int pageIndex, int pageSize);


    /**
     * @param processInstance
     * @param deleteReason
     */
    void deleteProcessInstance(String processInstance, String deleteReason);
}
