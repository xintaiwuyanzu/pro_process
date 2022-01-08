package com.dr.framework.core.process.controller;

import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.common.page.Page;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.process.bo.ProcessInstance;
import com.dr.framework.core.process.bo.TaskInstance;
import com.dr.framework.core.process.query.TaskInstanceQuery;
import com.dr.framework.core.web.annotations.Current;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * 任务实例controller
 */
public abstract class AbstractTaskInstanceController extends BaseProcessController {

    /**
     * 启动流程
     *
     * @param definitionId
     * @param requestParams
     * @return
     */
    @PostMapping("/start")
    public ResultEntity<ProcessInstance> startAndSend(String definitionId, @RequestParam Map<String, Object> requestParams, @Current Person person) {
        return ResultEntity.success(getTaskInstanceService().start(definitionId, requestParams, person));
    }

    /**
     * 发送到下一环节
     *
     * @param taskInstanceId 环节实例Id
     * @param nextPersonId   下一环节人Id
     * @param comment        备注信息
     * @param requestParams  额外请求参数
     * @param person         当前登录人
     * @return
     */
    @PostMapping("send")
    public ResultEntity<String> complete(String taskInstanceId, String nextPersonId, String comment, @RequestParam Map<String, Object> requestParams, @Current Person person) {
        // getTaskInstanceService().complete(taskInstanceId, nextPersonId, comment, requestParams);
        return ResultEntity.success("发送成功！");
    }


    /**
     * 获取详细的任务信息
     *
     * @param id
     * @return
     */
    @GetMapping("/detail")
    public ResultEntity<TaskInstance> detail(String id) {
        return ResultEntity.success(getTaskInstanceService().taskInfo(id));
    }

    //查询
    @RequestMapping("/page")
    public ResultEntity page(@Current Person person, TaskInstanceQuery query, @RequestParam(defaultValue = "0") int pageIndex, @RequestParam(defaultValue = Page.DEFAULT_PAGE_SIZE + "") int pageSize, @RequestParam(defaultValue = "true") boolean page) {
        query.assigneeEqual(person.getId());
        if (page) {
            return ResultEntity.success(getTaskInstanceService().taskPage(query, pageIndex, pageSize));
        } else {
            return ResultEntity.success(getTaskInstanceService().taskList(query));
        }
    }


    @RequestMapping("/history")
    public ResultEntity history(@Current Person person, TaskInstanceQuery query, @RequestParam(defaultValue = "0") int pageIndex, @RequestParam(defaultValue = Page.DEFAULT_PAGE_SIZE + "") int pageSize, @RequestParam(defaultValue = "true") boolean page) {
        query.assigneeEqual(person.getId());
        if (page) {
            return ResultEntity.success(getTaskInstanceService().taskHistoryPage(query, pageIndex, pageSize));
        } else {
            return ResultEntity.success(getTaskInstanceService().taskHistoryList(query));
        }
    }


}
