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
    private String title;
    /**
     * 流程实例描述
     */
    private String detail;
    /**
     * 流程实例创建人
     */
    private String createPerson;
    /**
     * TODO
     * 环节实例接收人
     */
    private String taskPerson;

    public ProcessInstanceQuery nameLike(String name) {
        this.title = name;
        return this;
    }

    public ProcessInstanceQuery descriptionLike(String description) {
        this.detail = description;
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

    public String getTitle() {
        return title;
    }

    public String getDetail() {
        return detail;
    }

    public String getCreatePerson() {
        return createPerson;
    }

    public String getTaskPerson() {
        return taskPerson;
    }
}
