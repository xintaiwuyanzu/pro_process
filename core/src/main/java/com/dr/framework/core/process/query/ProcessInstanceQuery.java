package com.dr.framework.core.process.query;

/**
 * 流程实例查询工具类
 *  TODO 起止时间
 *
 * @author dr
 */
public class ProcessInstanceQuery extends AbsProcessQuery<ProcessInstanceQuery> {
    /**
     * 流程名称
     */
    private String name;
    /**
     * 流程类型
     */
    private String type;
    /**
     * 流程实例描述
     */
    private String description;
    /**
     * 流程实例创建人
     */
    private String createPerson;
    /**
     * 环节实例接收人
     */
    private String taskPerson;

    public ProcessInstanceQuery nameLike(String name) {
        this.name = name;
        return this;
    }

    public ProcessInstanceQuery typeLike(String type) {
        this.type = type;
        return this;
    }

    public ProcessInstanceQuery descriptionLike(String description) {
        this.description = description;
        return this;
    }

    public ProcessInstanceQuery createPersonEqual(String createPerson) {
        this.createPerson = createPerson;
        return this;
    }

    public ProcessInstanceQuery taskPersonEqual(String taskPerson) {
        this.taskPerson = taskPerson;
        return this;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public String getCreatePerson() {
        return createPerson;
    }

    public String getTaskPerson() {
        return taskPerson;
    }
}
