package com.dr.framework.core.process.vo;

import com.dr.framework.core.security.bo.PermissionResource;

/**
 * 抽象父类，没啥用处，只是用来统一接口
 */
public abstract class AbstractProcessPermissionResource implements PermissionResource {
    @Override
    public void setId(String id) {
        //因为流程类型的资源是封装类，所以该方法没啥用处，这个是PermissionResource接口定义的问题
    }
}
