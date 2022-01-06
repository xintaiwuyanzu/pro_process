package com.dr.process.camunda.manager;

import org.camunda.bpm.engine.impl.interceptor.Session;
import org.camunda.bpm.engine.impl.interceptor.SessionFactory;

import java.util.function.Supplier;

/**
 * 自定义实现SessionFactory
 *
 * @author dr
 */
public class GenericAndCustomerImplManagerFactory implements SessionFactory {
    private Class sessionClass;
    private Supplier<Session> implObj;
    private Session object;

    public GenericAndCustomerImplManagerFactory(Class sessionClass, Supplier<Session> implObj) {
        this.sessionClass = sessionClass;
        this.implObj = implObj;
    }

    @Override
    public Class<?> getSessionType() {
        return sessionClass;
    }

    @Override
    public Session openSession() {
       /* if (object == null) {
            synchronized (this) {
                if (object == null) {
                    object =implObj.get() ;
                }
            }
        }*/
        //TODO 这里单例有问题？
        return implObj.get();
    }
}
