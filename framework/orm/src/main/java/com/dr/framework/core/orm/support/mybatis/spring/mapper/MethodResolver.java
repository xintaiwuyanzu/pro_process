package com.dr.framework.core.orm.support.mybatis.spring.mapper;

import java.lang.reflect.Method;

public class MethodResolver extends org.apache.ibatis.builder.annotation.MethodResolver {

    private MyBatisMapperAnnotationBuilder annotationBuilder;
    private Method method;

    public MethodResolver(MyBatisMapperAnnotationBuilder annotationBuilder, Method method) {
        super(null, null);
        this.annotationBuilder = annotationBuilder;
        this.method = method;
    }

    @Override
    public void resolve() {
        annotationBuilder.parseStatement(method);
    }
}
