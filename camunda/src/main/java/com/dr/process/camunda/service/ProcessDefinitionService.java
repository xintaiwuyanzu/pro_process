package com.dr.process.camunda.service;

import com.dr.framework.common.page.Page;
import com.dr.framework.core.process.bo.ProcessDefinition;
import com.dr.framework.core.util.Constants;

import java.io.InputStream;
import java.util.List;

/**
 * 表单定义接口声明
 *
 * @author dr
 */
public interface ProcessDefinitionService {
    boolean DEFAULT_LATEST_VERSION = true;
    String DEFAULT_DEPLOY_NAME = "DEPLOY BY ProcessDefinitionService";

    /**
     * 部署流程定义
     * 一个bpmn文件可能定义了多个流程定义
     *
     * @param stream
     * @return
     */
    default List<ProcessDefinition> deploy(InputStream stream) {
        return deploy(DEFAULT_DEPLOY_NAME, stream);
    }

    default List<ProcessDefinition> deploy(String deployName, InputStream stream) {
        return deploy(Constants.DEFAULT, deployName, stream);
    }

    /**
     * 部署流程定义
     *
     * @param type       流程类型
     * @param deployName 流程名称
     * @param stream     流程xml文件流
     * @return
     */
    List<ProcessDefinition> deploy(String type, String deployName, InputStream stream);


    /**
     * 根据流程定义Id删除流程定义
     *
     * @param id
     */
    void deleteProcessByDefinitionId(String id);

    /**
     * 根据流程定义编码删除流程定义
     * 可能删除多个
     *
     * @param defKey
     */
    void deleteProcessByDefinitionKey(String defKey);

    /**
     * 根据表单定义查询表单
     *
     * @param id
     * @return
     */
    ProcessDefinition selectById(String id);

    /**
     * 根据条件统计
     *
     * @param processDefinition
     * @return
     */
    default long count(ProcessDefinition processDefinition) {
        return count(processDefinition, DEFAULT_LATEST_VERSION);
    }

    /**
     * @param processDefinition
     * @param latestVersion     是否只查询最新版本
     * @return
     */
    long count(ProcessDefinition processDefinition, boolean latestVersion);

    /**
     * 根据条件查询列表
     *
     * @param processDefinition
     * @return
     */
    default List<ProcessDefinition> selectList(ProcessDefinition processDefinition) {
        return selectList(processDefinition, DEFAULT_LATEST_VERSION);
    }

    /**
     * 根据条件查询列表
     *
     * @param processDefinition
     * @param latestVersion
     * @return
     */
    List<ProcessDefinition> selectList(ProcessDefinition processDefinition, boolean latestVersion);

    default Page<ProcessDefinition> selectPage(ProcessDefinition processDefinition, int pageIndex, int pageSize) {
        return selectPage(processDefinition, pageIndex, pageSize, DEFAULT_LATEST_VERSION);
    }

    /**
     * 根据条件查询分页
     *
     * @param processDefinition
     * @param pageIndex
     * @param pageSize
     * @return
     */
    Page<ProcessDefinition> selectPage(ProcessDefinition processDefinition, int pageIndex, int pageSize, boolean latestVersion);


}
