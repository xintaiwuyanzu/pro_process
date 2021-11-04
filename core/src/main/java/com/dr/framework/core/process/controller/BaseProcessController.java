package com.dr.framework.core.process.controller;

import com.dr.framework.core.process.service.ProcessService;
import org.springframework.context.support.ApplicationObjectSupport;

/**
 * 流程controller基础父类，用来实现通用方法
 *
 * @author dr
 */
public class BaseProcessController extends ApplicationObjectSupport {
    /**
     * 流程汇总service
     */
    private ProcessService processService;

    public BaseProcessController(ProcessService processService) {
        this.processService = processService;
    }

    public ProcessService getProcessService() {
        return processService;
    }
}
