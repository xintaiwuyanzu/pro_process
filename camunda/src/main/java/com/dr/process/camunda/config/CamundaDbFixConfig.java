package com.dr.process.camunda.config;

import com.dr.framework.core.orm.database.DataBaseMetaData;
import com.dr.process.camunda.manager.CustomHistoricProcessInstanceManager;
import com.dr.process.camunda.manager.CustomTaskManager;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.session.Configuration;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.db.sql.DbSqlSessionFactory;
import org.camunda.bpm.engine.impl.interceptor.SessionFactory;
import org.camunda.bpm.engine.impl.persistence.entity.HistoricProcessInstanceManager;
import org.camunda.bpm.engine.impl.persistence.entity.TaskManager;
import org.camunda.bpm.engine.spring.SpringProcessEngineConfiguration;
import org.camunda.bpm.spring.boot.starter.configuration.CamundaDatasourceConfiguration;
import org.camunda.bpm.spring.boot.starter.property.CamundaBpmProperties;
import org.camunda.bpm.spring.boot.starter.property.DatabaseProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 用来解决数据库的问题
 *
 * @author dr
 */
public class CamundaDbFixConfig implements CamundaDatasourceConfiguration {
    protected static final Logger logger = LoggerFactory.getLogger(CamundaDbFixConfig.class);

    private PlatformTransactionManager transactionManager;
    private DataSource dataSource;
    private CamundaBpmProperties properties;

    public CamundaDbFixConfig(PlatformTransactionManager transactionManager,
                              DataSource dataSource,
                              CamundaBpmProperties properties) {
        this.transactionManager = transactionManager;
        this.dataSource = dataSource;
        this.properties = properties;
    }

    @Override
    public void preInit(ProcessEngineConfigurationImpl processEngineConfiguration) {
        if (processEngineConfiguration instanceof SpringProcessEngineConfiguration) {
            SpringProcessEngineConfiguration configuration = (SpringProcessEngineConfiguration) processEngineConfiguration;
            configuration.setTransactionManager(transactionManager);
            configuration.setDataSource(dataSource);
            DatabaseProperty database = properties.getDatabase();
            configuration.setDatabaseType(database.getType());
            configuration.setDatabaseSchemaUpdate(database.getSchemaUpdate());
            if (!StringUtils.isEmpty(database.getTablePrefix())) {
                configuration.setDatabaseTablePrefix(database.getTablePrefix());
            }
            configuration.setJdbcBatchProcessing(database.isJdbcBatchProcessing());
        }
        DataBaseMetaData dataBaseMetaData = new DataBaseMetaData(dataSource, "Camunda");
        String schema = Optional.ofNullable(dataBaseMetaData.getSchema()).orElse(dataBaseMetaData.getCatalog());
        if (!StringUtils.isEmpty(schema)) {
            //TODO?
            processEngineConfiguration.setDatabaseSchema(schema);
            processEngineConfiguration.setDatabaseTablePrefix(schema + ".");
        }

        //这里的taskManager 改成自己的
        List<SessionFactory> sessionFactories = processEngineConfiguration.getCustomSessionFactories();
        if (sessionFactories == null) {
            sessionFactories = new ArrayList<>();
            processEngineConfiguration.setCustomSessionFactories(sessionFactories);
        }

        sessionFactories.add(new GenericAndCustomerImplManagerFactory(TaskManager.class, new CustomTaskManager()));
        sessionFactories.add(new GenericAndCustomerImplManagerFactory(HistoricProcessInstanceManager.class, new CustomHistoricProcessInstanceManager()));
    }


    @Override
    public void postInit(ProcessEngineConfigurationImpl processEngineConfiguration) {
        fixDataBaseInfo(processEngineConfiguration);
        fixFixMybatis(processEngineConfiguration);
    }

    /**
     * 修复数据库相关的信息
     *
     * @param configuration
     */
    private void fixDataBaseInfo(ProcessEngineConfigurationImpl configuration) {
        //TODO? oracle的问题?
        if (DbSqlSessionFactory.ORACLE.equalsIgnoreCase(configuration.getDatabaseType())) {
            configuration.setJdbcBatchProcessing(false);
        }
    }

    /**
     * 添加自定义的sql语句
     *
     * @param configuration
     */
    private void fixFixMybatis(ProcessEngineConfigurationImpl configuration) {
        DbSqlSessionFactory dbSqlSessionFactory = configuration.getDbSqlSessionFactory();
        // dbSqlSessionFactory.setDatabaseSchema(dataBaseMetaData.getSchema());
        Configuration mybatisConfig = dbSqlSessionFactory.getSqlSessionFactory()
                .getConfiguration();
        loadXmlMapper(mybatisConfig, "Task");
        loadXmlMapper(mybatisConfig, "HistoricProcessInstance");
    }


    private void loadXmlMapper(Configuration configuration, String xml) {
        xml = "com/dr/process/camunda/mapping/entity/" + xml + ".xml";
        try {
            new XMLMapperBuilder(
                    new ClassPathResource(xml).getInputStream()
                    , configuration
                    , xml
                    , configuration.getSqlFragments()
            ).parse();
        } catch (IOException e) {
            logger.warn("加载自定义Camunda流程 sql xml失败{}:{}", xml, e.getMessage());
        }
    }

}
