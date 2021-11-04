package com.dr.framework.core.process.controller;

import com.dr.framework.core.process.service.ProcessService;

/**
 * 流程实例controller
 *
 * @author dr
 */
public abstract class AbstractProcessInstanceController extends BaseProcessController {
    public AbstractProcessInstanceController(ProcessService processService) {
        super(processService);
    }
}
