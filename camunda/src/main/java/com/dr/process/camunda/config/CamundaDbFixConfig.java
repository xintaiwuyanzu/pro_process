package com.dr.process.camunda.config;

import com.dr.framework.core.orm.database.DataBaseMetaData;
import com.dr.process.camunda.manager.DbEntityManagerWithSqlProxy;
import com.dr.process.camunda.command.process.definition.extend.ProcessDefinitionQueryImplWithExtend;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.session.Configuration;
import org.camunda.bpm.engine.impl.RepositoryServiceImpl;
import org.camunda.bpm.engine.impl.cfg.IdGenerator;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.context.Context;
import org.camunda.bpm.engine.impl.db.PersistenceSession;
import org.camunda.bpm.engine.impl.db.entitymanager.DbEntityManager;
import org.camunda.bpm.engine.impl.db.sql.DbSqlSessionFactory;
import org.camunda.bpm.engine.impl.interceptor.SessionFactory;
import org.camunda.bpm.engine.repository.ProcessDefinitionQuery;
import org.camunda.bpm.engine.spring.SpringProcessEngineConfiguration;
import org.camunda.bpm.spring.boot.starter.configuration.CamundaDatasourceConfiguration;
import org.camunda.bpm.spring.boot.starter.property.CamundaBpmProperties;
import org.camunda.bpm.spring.boot.starter.property.DatabaseProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
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
public class CamundaDbFixConfig extends ApplicationObjectSupport implements CamundaDatasourceConfiguration {
    protected static final Logger logger = LoggerFactory.getLogger(CamundaDbFixConfig.class);
    protected static final String CUSTOM_MAPPER_PATH_PATTERN = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + "camunda/mapping/*.mapper.xml";

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
        fixDataSource(processEngineConfiguration);
        fixCustomerSessionFactory(processEngineConfiguration);

        //这里扩展了sql ，查询语句使用自定义实现
        processEngineConfiguration.setRepositoryService(new RepositoryServiceImpl() {
            @Override
            public ProcessDefinitionQuery createProcessDefinitionQuery() {
                return new ProcessDefinitionQueryImplWithExtend(commandExecutor);
            }
        });
    }

    /**
     * 处理多数据源的情况
     *
     * @param processEngineConfiguration
     */
    private void fixDataSource(ProcessEngineConfigurationImpl processEngineConfiguration) {
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
    }

    /**
     * 添加自定义sessionFactory
     *
     * @param processEngineConfiguration
     */
    private void fixCustomerSessionFactory(ProcessEngineConfigurationImpl processEngineConfiguration) {
        List<SessionFactory> sessionFactories = processEngineConfiguration.getCustomSessionFactories();
        if (sessionFactories == null) {
            sessionFactories = new ArrayList<>();
            processEngineConfiguration.setCustomSessionFactories(sessionFactories);
        }
        //这里修改成自己的DbEntityManager，结合自定义sql语句使用自定义sql查询数据
        sessionFactories.add(new GenericAndCustomerImplManagerFactory(DbEntityManager.class, () -> {
            IdGenerator idGenerator = processEngineConfiguration.getIdGenerator();
            PersistenceSession persistenceSession = Context.getCommandContext().getSession(PersistenceSession.class);
            return new DbEntityManagerWithSqlProxy(idGenerator, persistenceSession);
        }));
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
     * TODO 这里的扩展方法有问题，xml文件里面的sql是从jar包复制出来的，一旦依赖代码版本升级，xml里面代码就得重新检查
     * <p>
     * TODO 比较好的方法是拦截xml文件解析，动态织入扩展sql代码
     *
     * @param configuration
     */
    private void fixFixMybatis(ProcessEngineConfigurationImpl configuration) {
        DbSqlSessionFactory dbSqlSessionFactory = configuration.getDbSqlSessionFactory();
        Configuration mybatisConfig = dbSqlSessionFactory.getSqlSessionFactory()
                .getConfiguration();
        try {
            for (Resource resource : getApplicationContext().getResources(CUSTOM_MAPPER_PATH_PATTERN)) {
                new XMLMapperBuilder(
                        resource.getInputStream()
                        , mybatisConfig
                        , resource.getFilename()
                        , mybatisConfig.getSqlFragments()
                ).parse();
            }
        } catch (IOException e) {
            logger.warn("加载自定义Camunda流程 mapper.xml失败:{}", e.getMessage());
        }
    }

}
