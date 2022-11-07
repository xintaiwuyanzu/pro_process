package com.dr.framework.core.process.bo;

import com.dr.framework.core.organise.entity.Person;

import java.util.List;
import java.util.Set;

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
    /**
     * 前一个环节所有id
     */
    private Set<String> preIds;
    /**
     * 下一环节所有Id
     */
    private Set<String> nextIds;

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

    public Set<String> getPreIds() {
        return preIds;
    }

    public void setPreIds(Set<String> preIds) {
        this.preIds = preIds;
    }

    public Set<String> getNextIds() {
        return nextIds;
    }

    public void setNextIds(Set<String> nextIds) {
        this.nextIds = nextIds;
    }
}
