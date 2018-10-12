package com.dr.framework.core.orm.support.mybatis.spring;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertyName;
import org.springframework.boot.context.properties.source.ConfigurationPropertyNameAliases;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource;
import org.springframework.boot.jdbc.DatabaseDriver;
import org.springframework.boot.jdbc.XADataSourceWrapper;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import javax.sql.XADataSource;

/**
 * 用来动态创建datasource，有可能配置多个数据源
 */
public class DataSourceFactory implements FactoryBean<DataSource>, BeanClassLoaderAware, BeanFactoryAware, InitializingBean, DisposableBean {
    private DataSourceProperties properties;
    private ClassLoader classLoader;
    private boolean xa;
    private BeanFactory beanFactory;
    private String beanName;
    private DataSource dataSource;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (isXa()) {
            try {
                XADataSource xaDataSource = createXaDataSource();
                XADataSourceWrapper wrapper = beanFactory.getBean(XADataSourceWrapper.class);
                dataSource = wrapper.wrapDataSource(xaDataSource);
                if (dataSource instanceof AtomikosDataSourceBean) {
                    ((AtomikosDataSourceBean) dataSource).setMaxPoolSize(50);
                }
            } catch (Exception e) {
                throw new IllegalStateException("创建多数据源失败，请检查添加spring-jta相关的依赖或数据库驱动版本", e);
            }
        } else {
            dataSource = properties.initializeDataSourceBuilder().build();
        }
        if (dataSource instanceof BeanNameAware) {
            ((BeanNameAware) dataSource).setBeanName(beanName);
        }
        if (dataSource instanceof InitializingBean) {
            ((InitializingBean) dataSource).afterPropertiesSet();
        }
    }

    @Override
    public void destroy() throws Exception {
        if (dataSource != null) {
            if (dataSource instanceof DisposableBean) {
                ((DisposableBean) dataSource).destroy();
            }
        }
    }

    @Override
    public Class<?> getObjectType() {
        return DataSource.class;
    }

    @Override
    public DataSource getObject() {
        return dataSource;
    }

    private XADataSource createXaDataSource() {
        String className = this.properties.getXa().getDataSourceClassName();
        if (!StringUtils.hasLength(className)) {
            className = DatabaseDriver.fromJdbcUrl(this.properties.determineUrl()).getXaDataSourceClassName();
        }
        Assert.state(StringUtils.hasLength(className), "No XA DataSource class name specified");
        XADataSource dataSource = createXaDataSourceInstance(className);
        bindXaProperties(dataSource, this.properties);
        return dataSource;
    }

    private XADataSource createXaDataSourceInstance(String className) {
        try {
            Class<?> dataSourceClass = ClassUtils.forName(className, this.classLoader);
            Object instance = BeanUtils.instantiateClass(dataSourceClass);
            Assert.isInstanceOf(XADataSource.class, instance);
            return (XADataSource) instance;
        } catch (Exception ex) {
            throw new IllegalStateException("Unable to create XADataSource instance from '" + className + "'");
        }
    }

    private void bindXaProperties(XADataSource target, DataSourceProperties dataSourceProperties) {
        Binder binder = new Binder(getBinderSource(dataSourceProperties));
        binder.bind(ConfigurationPropertyName.EMPTY, Bindable.ofInstance(target));
    }

    private ConfigurationPropertySource getBinderSource(DataSourceProperties dataSourceProperties) {
        MapConfigurationPropertySource source = new MapConfigurationPropertySource();
        source.put("user", this.properties.determineUsername());
        source.put("password", this.properties.determinePassword());
        source.put("url", this.properties.determineUrl());
        source.putAll(dataSourceProperties.getXa().getProperties());
        ConfigurationPropertyNameAliases aliases = new ConfigurationPropertyNameAliases();
        aliases.addAliases("user", "username");
        return source.withAliases(aliases);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public boolean isXa() {
        return xa;
    }

    public void setXa(boolean xa) {
        this.xa = xa;
    }

    public DataSourceProperties getProperties() {
        return properties;
    }

    public void setProperties(DataSourceProperties properties) {
        this.properties = properties;
    }

    public void setBeanName(String name) {
        this.beanName = name;
    }

    public String getBeanName() {
        return beanName;
    }
}
