package com.dr.process.camunda.controller;

import com.dr.framework.core.process.controller.AbstractTaskInstanceController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 环节定义相关的功能
 *
 * @author dr
 */
@RestController
@RequestMapping("${common.api-path:/api}/taskInstance")
public class ProcessTaskInstanceController extends AbstractTaskInstanceController {

}
