package com.dr.framework.core.orm.support.mybatis.spring.mapper;

import org.apache.ibatis.session.Configuration;

import java.lang.reflect.Method;

public class MapperMethod extends org.apache.ibatis.binding.MapperMethod {
    public MapperMethod(Class<?> entityClass, Method method, Configuration config) {
        super(entityClass, method, config);
    }
}
