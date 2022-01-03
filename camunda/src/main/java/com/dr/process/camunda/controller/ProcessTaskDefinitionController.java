package com.dr.process.camunda.controller;

import com.dr.framework.core.process.controller.AbstractTaskDefinitionController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 环节实例相关方法
 *
 * @author dr
 */
@RestController
@RequestMapping("${common.api-path:/api}/taskDefinition")
public class ProcessTaskDefinitionController extends AbstractTaskDefinitionController {
}
