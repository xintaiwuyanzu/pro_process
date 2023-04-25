package com.dr.process.camunda.service;

import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.process.bo.ProcessDefinition;
import com.dr.framework.core.process.service.ProcessConstants;

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
        return deploy(ProcessConstants.DEFAULT_PROCESS_TYPE, stream);
    }

    /**
     * 部署流程定义
     *
     * @param type   流程类型
     * @param stream 流程流
     * @return
     */
    default List<ProcessDefinition> deploy(String type, InputStream stream) {
        return deploy(type, null, stream);
    }

    /**
     * 部署流程定义
     *
     * @param type         流程类型
     * @param resourceName 流资源名称
     * @param stream       流程xml文件流
     * @return
     */
    List<ProcessDefinition> deploy(String type, String resourceName, InputStream stream);


    /**
     * 根据流程定义Id查询xml文件流
     *
     * @param processDefinitionId
     * @return
     */
    InputStream getDeployResourceById(String processDefinitionId);

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
     * 根据流程定义id、环节定义id取对应环节角色下的人员列表
     *
     * @param processDefinitionId 流程定义id
     * @param taskDefinitionId    环节定义id
     * @return
     */
    List<Person> getPersonByTaskDefinitionId(String processDefinitionId, String taskDefinitionId);
}
