package com.dr.framework.core.process.controller;

import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.common.page.Page;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.process.bo.TaskInstance;
import com.dr.framework.core.process.query.TaskQuery;
import com.dr.framework.core.process.service.ProcessService;
import com.dr.framework.core.web.annotations.Current;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 任务实例controller
 */
public abstract class AbstractTaskInstanceController extends BaseProcessController {
    public static final String FORM_PREFIX = "form.";
    public static final String VALUE_PREFIX = "value.";

    public AbstractTaskInstanceController(ProcessService processService) {
        super(processService);
    }

    /**
     * 创建和启动流程
     *
     * @param id
     * @param request
     * @param person
     * @return
     */
    @PostMapping("/insert")
    public ResultEntity insert(String id,
                               HttpServletRequest request,
                               @Current Person person) {
        Assert.notNull(id, "流程【processId】参数不能为空");
        //表单参数
        Map<String, Object> formMap = WebUtils.getParametersStartingWith(request, FORM_PREFIX);
        //环境变量参数
        Map<String, Object> variMap = WebUtils.getParametersStartingWith(request, VALUE_PREFIX);
        //  return ResultEntity.success(getProcessService().start(id, formMap, variMap, person));
        return null;
    }

    //更新
    @RequestMapping("/update")
    public ResultEntity update(TaskInstance taskObject,
                               HttpServletRequest request,
                               @Current Person person) {
        //表单参数
        Map<String, Object> formMap = WebUtils.getParametersStartingWith(request, FORM_PREFIX);
        //环境变量参数
        Map<String, Object> variMap = WebUtils.getParametersStartingWith(request, VALUE_PREFIX);
        //return ResultEntity.success(getProcessService().update(taskObject, formMap, variMap, person));
        return null;
    }

    //获取详细的任务信息
    @GetMapping("/detail")
    public ResultEntity<TaskInstance> detail(String id) {
        return ResultEntity.success(getProcessService().taskInfo(id));
    }

    //查询
    @RequestMapping("/page")
    public ResultEntity page(@Current Person person,
                             TaskQuery query,
                             @RequestParam(defaultValue = "0") int pageIndex,
                             @RequestParam(defaultValue = Page.DEFAULT_PAGE_SIZE + "") int pageSize,
                             @RequestParam(defaultValue = "true") boolean page) {
        query.assigneeEqual(person.getId());
        if (page) {
            return ResultEntity.success(getProcessService().taskPage(query, pageIndex, pageSize));
        } else {
            return ResultEntity.success(getProcessService().taskList(query));
        }
    }


    @RequestMapping("/history")
    public ResultEntity history(@Current Person person,
                                TaskQuery query,
                                @RequestParam(defaultValue = "0") int pageIndex,
                                @RequestParam(defaultValue = Page.DEFAULT_PAGE_SIZE + "") int pageSize,
                                @RequestParam(defaultValue = "true") boolean page) {
        query.assigneeEqual(person.getId());
        if (page) {
            return ResultEntity.success(getProcessService().taskHistoryPage(query, pageIndex, pageSize));
        } else {
            return ResultEntity.success(getProcessService().taskHistoryList(query));
        }
    }
}
