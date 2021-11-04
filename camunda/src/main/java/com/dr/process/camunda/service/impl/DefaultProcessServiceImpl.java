package com.dr.process.camunda.service.impl;

import com.dr.framework.core.process.bo.ProcessDefinition;
import com.dr.framework.core.process.service.ProcessDefinitionService;
import com.dr.framework.core.process.service.ProcessInstanceService;
import com.dr.framework.core.process.service.TaskDefinitionService;
import com.dr.framework.core.process.service.TaskInstanceService;
import com.dr.framework.core.process.service.impl.ProcessServiceComposite;
import com.dr.process.camunda.service.ProcessDeployService;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

import java.io.InputStream;
import java.util.List;

/**
 * camunda 流程引擎默认实现
 *
 * @author dr
 */
public class DefaultProcessServiceImpl extends BaseProcessServiceImpl implements ProcessServiceComposite, ProcessDeployService {
    private ProcessDefinitionService processDefinitionService;
    private ProcessInstanceService processInstanceService;
    private TaskDefinitionService taskDefinitionService;
    private TaskInstanceService taskInstanceService;
    private ProcessDeployService processDeployService;

    public DefaultProcessServiceImpl(ProcessEngineConfigurationImpl processEngineConfiguration) {
        super(processEngineConfiguration);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        //初始化内置service对象
        AutowireCapableBeanFactory autowireCapableBeanFactory = getApplicationContext().getAutowireCapableBeanFactory();
        processDefinitionService = createBean(autowireCapableBeanFactory, new DefaultProcessDefinitionServiceImpl(getProcessEngineConfiguration()));
        processInstanceService = createBean(autowireCapableBeanFactory, new DefaultProcessInstanceServiceImpl(getProcessEngineConfiguration()));
        taskDefinitionService = createBean(autowireCapableBeanFactory, new DefaultTaskDefinitionServiceImpl(getProcessEngineConfiguration()));
        taskInstanceService = createBean(autowireCapableBeanFactory, new DefaultTaskInstanceServiceImpl(getProcessEngineConfiguration(), processDefinitionService));
        processDeployService = createBean(autowireCapableBeanFactory, new DefaultProcessDeployServiceImpl(getProcessEngineConfiguration()));
    }

    /**
     * 创建内部service
     *
     * @param autowireCapableBeanFactory
     * @param bean
     * @param <T>
     * @return
     */
    protected <T> T createBean(AutowireCapableBeanFactory autowireCapableBeanFactory, T bean) {
        String beanName = bean.getClass().getName();
        bean = (T) autowireCapableBeanFactory.applyBeanPostProcessorsBeforeInitialization(bean, beanName);
        bean = (T) autowireCapableBeanFactory.initializeBean(bean, beanName);
        bean = (T) autowireCapableBeanFactory.applyBeanPostProcessorsAfterInitialization(bean, beanName);
        return bean;
    }

    @Override
    public ProcessDefinitionService getProcessDefinitionService() {
        return processDefinitionService;
    }

    @Override
    public ProcessInstanceService getProcessInstanceService() {
        return processInstanceService;
    }

    @Override
    public TaskDefinitionService getTaskDefinitionService() {
        return taskDefinitionService;
    }

    @Override
    public TaskInstanceService getTaskInstanceService() {
        return taskInstanceService;
    }

    public ProcessDeployService getProcessDeployService() {
        return processDeployService;
    }

    @Override
    public List<ProcessDefinition> deploy(String type, String resourceName, InputStream stream) {
        return getProcessDeployService().deploy(type, resourceName, stream);
    }

    @Override
    public InputStream getDeployResourceById(String processDefinitionId) {
        return getProcessDeployService().getDeployResourceById(processDefinitionId);
    }

    @Override
    public void deleteProcessByDefinitionId(String id) {
        getProcessDeployService().deleteProcessByDefinitionId(id);
    }

    @Override
    public void deleteProcessByDefinitionKey(String defKey) {
        getProcessDeployService().deleteProcessByDefinitionKey(defKey);

    }
}
