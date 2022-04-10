package com.dr.framework.core.process.service;

import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.process.bo.ProcessDefinition;
import com.dr.framework.core.process.bo.ProcessInstance;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 流程操作上下文
 *
 * @author dr
 */
public class ProcessContext {

    static String[] varParams = new String[]{ProcessConstants.VAR_NEXT_TASK_ID, ProcessConstants.VAR_NEXT_TASK_PERSON, ProcessConstants.VAR_COMMENT_KEY};
    /**
     * 当前登录人
     */
    private Person person;
    /**
     * 流程定义信息
     */
    private ProcessDefinition processDefinition;
    /**
     * 业务相关的其他参数
     */
    private Map<String, Object> businessParams = new HashMap<>();
    /**
     * 流程变量
     */
    private Map<String, Object> processVarMap = new HashMap<>();
    /**
     * 如果不是前端防范，这个值就是空的
     */
    private HttpServletRequest request;
    /**
     * 流程实例标题
     */
    private String processInstanceTitle;
    /**
     * 流程实例描述
     */
    private String processInstanceDetail;
    /**
     * 业务关联Id
     */
    private String businessId;
    /**
     * 流程实例
     */
    private ProcessInstance processInstance;

    public ProcessContext(Person person, ProcessDefinition processDefinition) {
        this.person = person;
        this.processDefinition = processDefinition;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public ProcessDefinition getProcessDefinition() {
        return processDefinition;
    }

    public void setProcessDefinition(ProcessDefinition processDefinition) {
        this.processDefinition = processDefinition;
    }

    public Map<String, Object> getBusinessParams() {
        return businessParams;
    }

    public void setBusinessParams(Map<String, Object> businessParams) {
        if (businessParams != null) {
            this.businessParams.putAll(businessParams);
            for (String varParam : varParams) {
                if (businessParams.containsKey(varParam) && !processVarMap.containsKey(varParam)) {
                    processVarMap.put(varParam, businessParams.get(varParam));
                }
            }
        }
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public String getProcessInstanceTitle() {
        return processInstanceTitle;
    }

    public void setProcessInstanceTitle(String processInstanceTitle) {
        this.processInstanceTitle = processInstanceTitle;
    }

    public String getProcessInstanceDetail() {
        return processInstanceDetail;
    }

    public void setProcessInstanceDetail(String processInstanceDetail) {
        this.processInstanceDetail = processInstanceDetail;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public ProcessInstance getProcessInstance() {
        return processInstance;
    }

    public void setProcessInstance(ProcessInstance processInstance) {
        this.processInstance = processInstance;
    }

    /**
     * 添加流程变量
     *
     * @param key
     * @param value
     */
    public void addVar(String key, Object value) {
        processVarMap.put(key, value);
    }

    public Map<String, Object> getProcessVarMap() {
        return processVarMap;
    }
}
