package com.dr.framework.core.process.controller;

import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.common.page.Page;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.process.query.ProcessInstanceQuery;
import com.dr.framework.core.web.annotations.Current;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 流程实例controller
 *
 * @author dr
 */
public abstract class AbstractProcessInstanceController extends BaseProcessController {
    /**
     * 查询运行中的流程
     *
     * @param person
     * @param query
     * @param pageIndex
     * @param pageSize
     * @param page
     * @return
     */
    @RequestMapping("page")
    public ResultEntity page(@Current Person person, ProcessInstanceQuery query, @RequestParam(defaultValue = "0") int pageIndex, @RequestParam(defaultValue = Page.DEFAULT_PAGE_SIZE + "") int pageSize, @RequestParam(defaultValue = "true") boolean page) {
        if (page) {
            return ResultEntity.success(getProcessInstanceService().processInstancePage(query, pageIndex, pageSize));
        } else {
            return ResultEntity.success(getProcessInstanceService().processInstanceList(query));
        }
    }

    @RequestMapping("historyPage")
    public ResultEntity historyPage(@Current Person person, ProcessInstanceQuery query, @RequestParam(defaultValue = "0") int pageIndex, @RequestParam(defaultValue = Page.DEFAULT_PAGE_SIZE + "") int pageSize, @RequestParam(defaultValue = "true") boolean page) {
        if (page) {
            return ResultEntity.success(getProcessInstanceService().processInstanceHistoryPage(query, pageIndex, pageSize));
        } else {
            return ResultEntity.success(getProcessInstanceService().processInstanceHistoryList(query));
        }
    }


}
