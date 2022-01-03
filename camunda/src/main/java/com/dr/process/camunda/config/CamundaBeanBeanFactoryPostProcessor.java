package com.dr.process.camunda.config;

import com.dr.framework.core.orm.support.mybatis.spring.DataSourceFactory;
import com.dr.framework.core.orm.support.mybatis.spring.boot.autoconfigure.MultiDataSourceProperties;
import com.dr.process.camunda.utils.Constants;
import org.camunda.bpm.spring.boot.starter.configuration.impl.DefaultJobConfiguration;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class CamundaBeanBeanFactoryPostProcessor implements BeanDefinitionRegistryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        //数据源相关代码
        String[] datasourceNames = beanFactory.getBeanNamesForType(DataSource.class, true, false);
        Map<BeanDefinition, String> dataSourceBeanDefinitions = new HashMap<>();
        for (String datasourceName : datasourceNames) {
            dataSourceBeanDefinitions.put(beanFactory.getBeanDefinition(datasourceName), datasourceName);
        }
        BeanDefinition datasource = getCamundaDatasource(dataSourceBeanDefinitions.keySet());
        Assert.isTrue(datasource != null, "未找到流程相关数据源定义");
        //手动设置流程数据源别名
        beanFactory.registerAlias(dataSourceBeanDefinitions.get(datasource), "camundaBpmDataSource");
    }

    private BeanDefinition getCamundaDatasource(Set<BeanDefinition> dataSourceBeanDefinitions) {
        if (dataSourceBeanDefinitions.size() == 1) {
            return dataSourceBeanDefinitions.iterator().next();
        }
        BeanDefinition primary = null;
        BeanDefinition taskBeanDefinition = null;
        for (BeanDefinition dataSourceBeanDefinition : dataSourceBeanDefinitions) {
            if (dataSourceBeanDefinition.isPrimary()) {
                primary = dataSourceBeanDefinition;
            }
            MutablePropertyValues mutablePropertyValues = dataSourceBeanDefinition.getPropertyValues();
            Object properties = mutablePropertyValues.get("properties");
            if (properties instanceof MultiDataSourceProperties && DataSourceFactory.class.getName().equals(dataSourceBeanDefinition.getBeanClassName())) {
                MultiDataSourceProperties multiDataSourceProperties = (MultiDataSourceProperties) properties;
                if (multiDataSourceProperties.containModules(Constants.MODULE_NAME)) {
                    taskBeanDefinition = dataSourceBeanDefinition;
                    break;
                }
            }
        }
        return Optional.ofNullable(taskBeanDefinition).orElse(primary);
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        if (registry.containsBeanDefinition(TaskExecutionAutoConfiguration.APPLICATION_TASK_EXECUTOR_BEAN_NAME) && registry.containsBeanDefinition(DefaultJobConfiguration.JobConfiguration.CAMUNDA_TASK_EXECUTOR_QUALIFIER)) {
            //spring上下文已经有applicationTaskExecutor了，删除掉camundaTaskExecutor
            registry.registerAlias(TaskExecutionAutoConfiguration.APPLICATION_TASK_EXECUTOR_BEAN_NAME, DefaultJobConfiguration.JobConfiguration.CAMUNDA_TASK_EXECUTOR_QUALIFIER);
            registry.removeBeanDefinition(DefaultJobConfiguration.JobConfiguration.CAMUNDA_TASK_EXECUTOR_QUALIFIER);
        }
    }
}