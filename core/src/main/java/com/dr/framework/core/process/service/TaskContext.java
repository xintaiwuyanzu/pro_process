package com.dr.framework.core.process.service;

import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.process.bo.ProcessDefinition;
import com.dr.framework.core.process.bo.TaskInstance;

/**
 * 环节上下文
 *
 * @author dr
 */
public class TaskContext extends ProcessContext {
    private TaskInstance taskInstance;

    public TaskContext(Person person, ProcessDefinition processDefinition, TaskInstance taskInstance) {
        super(person, processDefinition);
        this.taskInstance = taskInstance;
    }

    public TaskInstance getTaskInstance() {
        return taskInstance;
    }
}
