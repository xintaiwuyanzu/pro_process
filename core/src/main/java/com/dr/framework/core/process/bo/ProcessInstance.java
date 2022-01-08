package com.dr.framework.core.process.bo;

import java.util.Map;

/**
 * 流程定义对象
 *
 * @author dr
 */
public class ProcessInstance extends ProcessDefinition {
    /**
     * 流程实例名称
     */
    private String title;
    /**
     * 流程实例描述
     */
    private String detail;
    /**
     * 流程创建时间
     */
    private long createDate;
    /**
     * 流程创建人
     */
    private String createPerson;
    /**
     * 流程创建人名称
     */
    private String createPersonName;
    /**
     * 流程定义ID
     */
    private String processDefineId;

    /**
     * 流程结束时间
     */
    private long endDate;
    /**
     * 流程是否暂停
     */
    private boolean suspend;
    /**
     * 表单跳转地址
     */
    private String formUrl;

    /**
     * 流程运行时的环境变量
     */
    private Map<String, Object> variables;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public String getCreatePerson() {
        return createPerson;
    }

    public void setCreatePerson(String createPerson) {
        this.createPerson = createPerson;
    }

    public String getCreatePersonName() {
        return createPersonName;
    }

    public void setCreatePersonName(String createPersonName) {
        this.createPersonName = createPersonName;
    }

    public String getProcessDefineId() {
        return processDefineId;
    }

    public void setProcessDefineId(String processDefineId) {
        this.processDefineId = processDefineId;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public boolean isSuspend() {
        return suspend;
    }

    public void setSuspend(boolean suspend) {
        this.suspend = suspend;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, Object> variables) {
        this.variables = variables;
    }

    public String getFormUrl() {
        return formUrl;
    }

    public void setFormUrl(String formUrl) {
        this.formUrl = formUrl;
    }
}
