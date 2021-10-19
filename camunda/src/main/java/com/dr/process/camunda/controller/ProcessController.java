package com.dr.process.camunda.controller;

import com.dr.framework.core.process.controller.AbstractProcessController;
import com.dr.framework.core.process.service.ProcessService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 流程控制器
 *
 * @author dr
 */
@RestController
@RequestMapping("${common.api-path:/api}/process")
public class ProcessController extends AbstractProcessController {


    public ProcessController(ProcessService processService) {
        super(processService);
    }
}
