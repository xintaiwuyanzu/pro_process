package com.dr.framework.core.orm.support.mybatis.spring.mapper;

import com.dr.framework.core.orm.annotations.Mapper;
import com.dr.framework.core.orm.support.mybatis.spring.MybatisConfigurationBean;
import com.dr.framework.core.orm.support.mybatis.spring.mapper.method.MapperMethod;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MultiMapperProxy extends AbstractMapperProxy {
    private Map<String, MybatisConfigurationBean> mybatisConfigurationBeanMap;
    private final Map<MybatisConfigurationBean, Map<Method, MapperMethod>> methodCache;
    /**
     * 默认主要数据源
     */
    private MybatisConfigurationBean primaryBean;

    public MultiMapperProxy(MybatisConfigurationBean primaryBean, Map<String, MybatisConfigurationBean> mybatisConfigurationBeanMap, Class<?> mapperInterface) {
        super(mapperInterface);
        this.mybatisConfigurationBeanMap = mybatisConfigurationBeanMap;
        methodCache = new ConcurrentHashMap<>(mybatisConfigurationBeanMap.size());
        this.primaryBean = primaryBean;
    }

    @Override
    protected MapperMethod cachedMapperMethod(Method method, Class entityClass, MybatisConfigurationBean mybatisConfigurationBean) {
        Map<Method, MapperMethod> methodMapperMethodMap = methodCache.get(mybatisConfigurationBean);
        if (methodMapperMethodMap == null) {
            methodMapperMethodMap = new ConcurrentHashMap<>(mapperInterface.getDeclaredMethods().length);
            methodCache.put(mybatisConfigurationBean, methodMapperMethodMap);
        }
        MapperMethod mapperMethod = methodMapperMethodMap.get(method);
        if (mapperMethod == null) {
            mapperMethod = new MapperMethod(mapperInterface, method, mybatisConfigurationBean);
            methodMapperMethodMap.put(method, mapperMethod);
        }
        return mapperMethod;
    }

    @Override
    protected MybatisConfigurationBean findConfigBean(Method method, Class entityClass) {
        if (entityClass != null) {
            for (MybatisConfigurationBean mybatisConfigurationBean : mybatisConfigurationBeanMap.values()) {
                if (mybatisConfigurationBean.containsEntity(entityClass)) {
                    return mybatisConfigurationBean;
                }
            }
        } else {
            Class mapperInterface = method.getDeclaringClass();
            if (mapperInterface.isAnnotationPresent(Mapper.class)) {
                Mapper mapper = (Mapper) mapperInterface.getAnnotation(Mapper.class);
                for (String module : mapper.module()) {
                    for (MybatisConfigurationBean mybatisConfigurationBean : mybatisConfigurationBeanMap.values()) {
                        if (mybatisConfigurationBean.containModules(module)) {
                            return mybatisConfigurationBean;
                        }
                    }
                }
            }
        }
        return primaryBean;
    }
}
