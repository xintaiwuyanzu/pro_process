package com.dr.process.camunda.config;

import org.camunda.bpm.engine.impl.interceptor.Session;
import org.camunda.bpm.engine.impl.interceptor.SessionFactory;
import org.springframework.cglib.core.ReflectUtils;

/**
 * 自定义实现SessionFactory
 *
 * @author dr
 */
class GenericAndCustomerImplManagerFactory implements SessionFactory {
    private Class sessionClass;
    private Session implObj;

    public GenericAndCustomerImplManagerFactory(Class sessionClass, Session implObj) {
        this.sessionClass = sessionClass;
        this.implObj = implObj;
    }

    @Override
    public Class<?> getSessionType() {
        return sessionClass;
    }

    @Override
    public Session openSession() {
        return implObj;
    }
}
