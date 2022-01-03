package com.dr.framework.core.process.bo;

import com.dr.framework.core.organise.entity.Person;

import java.util.List;

/**
 * @author 环节定义信息
 */
public class TaskDefinition extends AbstractProcessObject {
    /**
     * 启动人员
     */
    private List<Person> startUser;
    /**
     * 定义的流程属性
     */
    private List<Property> processProPerties;

    public List<Person> getStartUser() {
        return startUser;
    }

    public void setStartUser(List<Person> startUser) {
        this.startUser = startUser;
    }

    public List<Property> getProcessProPerties() {
        return processProPerties;
    }

    public void setProcessProPerties(List<Property> processProPerties) {
        this.processProPerties = processProPerties;
    }
}