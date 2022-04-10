package com.dr.framework.core.process.service;

import com.dr.framework.common.page.Page;
import com.dr.framework.core.process.bo.ProcessDefinition;
import com.dr.framework.core.process.query.ProcessDefinitionQuery;

import java.util.List;

/**
 * 流程定义service
 *
 * @author dr
 */
public interface ProcessDefinitionService {

    /**
     * 根据id查询流程定义
     *
     * @param id
     * @return
     */
    ProcessDefinition getProcessDefinitionById(String id);

    /**
     * 根据key查询流程定义
     *
     * @param key
     * @return
     */
    ProcessDefinition getProcessDefinitionByKey(String key);


    /**
     * @param query
     * @return
     */
    long countDefinition(ProcessDefinitionQuery query);

    /**
     * 查询流程定义
     *
     * @param processDefinitionQuery
     * @return
     */
    List<ProcessDefinition> processDefinitionList(ProcessDefinitionQuery processDefinitionQuery);

    /**
     * 查询流程定义分页
     *
     * @param processDefinitionQuery
     * @param pageIndex
     * @param pageSize
     * @return
     */
    Page<ProcessDefinition> processDefinitionPage(ProcessDefinitionQuery processDefinitionQuery, int pageIndex, int pageSize);
}
