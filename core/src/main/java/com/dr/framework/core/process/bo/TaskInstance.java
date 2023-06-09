package com.dr.framework.core.process.bo;

import java.util.List;
import java.util.Map;

/**
 * @author dr
 */
public class TaskInstance<T> extends ProcessInstance {

    /**
     * =================================================
     * 上面是流程相关的属性
     * =================================================
     */
    /**
     * 流程实例Id
     */
    private String processInstanceId;
    /**
     * 流程实例变量
     */
    private Map<String, Object> processVariables;
    /**
     * 流程定义的扩展属性
     */
    private List<Property> processProPerties;
    /**
     * 审核意见
     */
    private List<Comment> comments;
    /**
     * 只有一个审核意见
     */
    private Comment comment;
    /**
     * 环节定义id
     */
    private String taskDefineKey;
    /**
     * 环节创建人Id
     */
    private String owner;
    /**
     * 环节创建人名称
     */
    private String ownerName;
    /**
     * 环节受理人
     */
    private String assignee;
    /**
     * 环节受理人名称
     */
    private String assigneeName;

    /**
     * =================================================
     * 下面是业务相关的属性
     * =================================================
     */
    /**
     * 表单Id
     */
    private String formKey;

    /**
     * 表单数据
     * TODO
     */
    private T form;


    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }


    public String getTaskDefineKey() {
        return taskDefineKey;
    }

    public void setTaskDefineKey(String taskDefineKey) {
        this.taskDefineKey = taskDefineKey;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }


    public String getFormKey() {
        return formKey;
    }

    public void setFormKey(String formKey) {
        this.formKey = formKey;
    }


    public T getForm() {
        return form;
    }

    public void setForm(T form) {
        this.form = form;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getAssigneeName() {
        return assigneeName;
    }

    public void setAssigneeName(String assigneeName) {
        this.assigneeName = assigneeName;
    }

    public List<Property> getProcessProPerties() {
        return processProPerties;
    }

    public void setProcessProPerties(List<Property> processProPerties) {
        this.processProPerties = processProPerties;
    }

    public Map<String, Object> getProcessVariables() {
        return processVariables;
    }

    public void setProcessVariables(Map<String, Object> processVariables) {
        this.processVariables = processVariables;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
        if (comments != null && !comments.isEmpty()) {
            comment = comments.get(0);
        }
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }
}
