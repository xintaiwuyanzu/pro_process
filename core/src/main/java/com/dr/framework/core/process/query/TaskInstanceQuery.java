package com.dr.framework.core.process.query;

/**
 * 用来查询流程任务
 *
 * @author dr
 */
public class TaskInstanceQuery extends ProcessInstanceQuery {
    /**
     * 流程创建人
     */
    private String createPerson;
    /**
     * 环节创建人
     */
    private String owner;
    /**
     * 环节受理人
     */
    private String assignee;

    /**
     * 环节定义Id
     */
    private String taskKeyLike;

    /**
     * 环节描述
     */
    private String description;

    /**
     * 流程实例Id
     */
    private String processInstanceId;
    private String processDefinitionKey;
    /**
     * 是否查询环节扩展属性
     */
    private boolean withVariables;
    /**
     * 是否查询流程实例变量
     */
    private boolean withProcessVariables;
    /**
     * 是否查询流程扩展属性
     */
    private boolean withProcessProperty;

    public TaskInstanceQuery processInstanceIdEqual(String processInstanceId) {
        this.processInstanceId = processInstanceId;
        return this;
    }

    @Override
    public TaskInstanceQuery createPersonEqual(String createPerson) {
        this.createPerson = createPerson;
        return this;
    }

    public TaskInstanceQuery ownerEqual(String owner) {
        this.owner = owner;
        return this;
    }

    public TaskInstanceQuery assigneeEqual(String assignee) {
        this.assignee = assignee;
        return this;
    }

    public TaskInstanceQuery taskKeyLike(String taskKeyLike) {
        this.taskKeyLike = taskKeyLike;
        return this;
    }

    public TaskInstanceQuery withVariables() {
        this.withVariables = true;
        return this;
    }


    @Override
    public TaskInstanceQuery descriptionLike(String description) {
        this.description = description;
        return this;
    }

    public TaskInstanceQuery withProcessProperty(boolean withProcessProperty) {
        this.withProcessProperty = withProcessProperty;
        return this;
    }

    public TaskInstanceQuery processDefinitionKeyLike(String processDefinitionKey) {
        this.processDefinitionKey = processDefinitionKey;
        return this;
    }

    public String getOwner() {
        return owner;
    }

    @Override
    public String getCreatePerson() {
        return createPerson;
    }

    public String getAssignee() {
        return assignee;
    }

    public String getTaskKeyLike() {
        return taskKeyLike;
    }

    public String getDescription() {
        return description;
    }

    public boolean isWithVariables() {
        return withVariables;
    }

    public void setWithVariables(boolean withVariables) {
        this.withVariables = withVariables;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public boolean isWithProcessProperty() {
        return withProcessProperty;
    }

    public void setWithProcessProperty(boolean withProcessProperty) {
        this.withProcessProperty = withProcessProperty;
    }

    public String getProcessDefinitionKey() {
        return processDefinitionKey;
    }

    public boolean isWithProcessVariables() {
        return withProcessVariables;
    }

    public void setWithProcessVariables(boolean withProcessVariables) {
        this.withProcessVariables = withProcessVariables;
    }
}
