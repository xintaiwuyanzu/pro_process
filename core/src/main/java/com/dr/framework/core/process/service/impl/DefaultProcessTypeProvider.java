package com.dr.framework.core.process.service.impl;

import com.dr.framework.core.process.service.ProcessConstants;
import com.dr.framework.core.process.service.ProcessContext;
import com.dr.framework.core.process.service.ProcessTypeProvider;

/**
 * 默认流程类型提供器
 *
 * @author dr
 */
public class DefaultProcessTypeProvider implements ProcessTypeProvider {

    @Override
    public String getFormUrl(ProcessContext context) {
        return "/process/taskTodo/detail";
    }

    @Override
    public String getType() {
        return ProcessConstants.DEFAULT_PROCESS_TYPE;
    }

    @Override
    public String getName() {
        return "默认类型";
    }
}
