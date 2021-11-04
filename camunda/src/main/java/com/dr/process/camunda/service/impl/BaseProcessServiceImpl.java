package com.dr.process.camunda.service.impl;

import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.interceptor.CommandExecutor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.support.ApplicationObjectSupport;

/**
 * 流程定义抽象父类
 *
 * @author dr
 */
public abstract class BaseProcessServiceImpl extends ApplicationObjectSupport implements InitializingBean {
    private ProcessEngineConfigurationImpl processEngineConfiguration;

    public BaseProcessServiceImpl(ProcessEngineConfigurationImpl processEngineConfiguration) {
        this.processEngineConfiguration = processEngineConfiguration;
    }

    public ProcessEngineConfigurationImpl getProcessEngineConfiguration() {
        return processEngineConfiguration;
    }

    public CommandExecutor getCommandExecutor() {
        return getProcessEngineConfiguration().getCommandExecutorTxRequired();
    }

    protected <T> T getBean(Class<T> clazz) {
        return processEngineConfiguration.getArtifactFactory().getArtifact(clazz);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
    }
}
