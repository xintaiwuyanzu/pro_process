package com.dr.framework.core.process.controller;

import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.common.entity.ResultListEntity;
import com.dr.framework.common.page.Page;
import com.dr.framework.core.process.bo.ProcessTypeProviderWrapper;
import com.dr.framework.core.process.query.ProcessDefinitionQuery;
import com.dr.framework.core.process.service.ProcessService;
import com.dr.framework.core.process.service.ProcessTypeProvider;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 暂时的流程管理统一入口
 *
 * @author dr
 */
public class AbstractProcessDefinitionController extends BaseProcessController implements InitializingBean {

    private List<ProcessTypeProvider> processTypeProviders = Collections.emptyList();

    public AbstractProcessDefinitionController(ProcessService processService) {
        super(processService);
    }

    /**
     * 查询流程定义分页
     *
     * @return
     */
    @PostMapping("/page")
    public ResultEntity page(ProcessDefinitionQuery query,
                             @RequestParam(defaultValue = "0") int pageIndex,
                             @RequestParam(defaultValue = Page.DEFAULT_PAGE_SIZE + "") int pageSize,
                             @RequestParam(defaultValue = "true") boolean page) {
        if (page) {
            return ResultEntity.success(getProcessService().processDefinitionPage(query, pageIndex, pageSize));
        } else {
            return ResultEntity.success(getProcessService().processDefinitionList(query));
        }
    }

    /**
     * 系统内置支持的流程类型
     * 供前端下拉选择使用
     *
     * @return
     */
    @GetMapping("/processType")
    public ResultListEntity<ProcessTypeProvider> processTypes() {
        return ResultListEntity.success(processTypeProviders);
    }

    @Override
    public void afterPropertiesSet() {
        processTypeProviders = getApplicationContext().getBeansOfType(ProcessTypeProvider.class)
                .values()
                .stream()
                .map(ProcessTypeProviderWrapper::new)
                .collect(Collectors.toList());
    }
}
