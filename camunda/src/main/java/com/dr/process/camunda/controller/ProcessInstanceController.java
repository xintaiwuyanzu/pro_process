package com.dr.process.camunda.controller;

import com.dr.framework.core.process.controller.AbstractProcessInstanceController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 流程实例前台接口
 *
 * @author dr
 */
@RestController
@RequestMapping("${common.api-path:/api}/processInstance")
public class ProcessInstanceController extends AbstractProcessInstanceController {
}
