package com.dr.framework.core.process.controller;

import com.dr.framework.common.entity.ResultListEntity;
import com.dr.framework.core.process.bo.TaskDefinition;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 环节定义相关controller
 *
 * @author dr
 */
public class AbstractTaskDefinitionController extends BaseProcessController {

    /**
     * 根据环节实例查询下一环节定义信息
     *
     * @param taskInstanceId
     * @return
     */
    @RequestMapping("nextTasks")
    public ResultListEntity<TaskDefinition> nextTasks(String taskInstanceId) {
        return ResultListEntity.success(getTaskDefinitionService().nextTasks(taskInstanceId));
    }

}
