package com.dr.process.camunda.service;

import com.dr.framework.core.process.bo.ProcessDefinition;
import com.dr.framework.core.util.Constants;

import java.io.InputStream;
import java.util.List;

/**
 * 表单定义接口声明
 *
 * @author dr
 */
public interface ProcessDeployService {
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

}
