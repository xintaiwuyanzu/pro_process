package com.dr.framework.core.process.service;

import com.dr.framework.core.util.Constants;

/**
 * 默认流程类型提供器
 *
 * @author dr
 */
public class DefaultProcessTypeProvider implements ProcessTypeProvider {

    @Override
    public String getType() {
        return Constants.DEFAULT;
    }

    @Override
    public String getName() {
        return "默认类型";
    }
}
