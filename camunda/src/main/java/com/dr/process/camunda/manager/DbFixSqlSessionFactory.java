package com.dr.process.camunda.manager;

import com.dr.framework.core.orm.database.DataBaseMetaData;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.db.sql.DbSqlSessionFactory;
import org.camunda.bpm.engine.impl.interceptor.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

/**
 * 有的数据库没办法直接获取到schema和catalog相关的信息
 *
 * @author dr
 */
public class DbFixSqlSessionFactory extends DbSqlSessionFactory {
    private DataBaseMetaData dataBaseMetaData;
    private ProcessEngineConfigurationImpl processEngineConfiguration;
    Logger logger = LoggerFactory.getLogger(DbFixSqlSessionFactory.class);

    public DbFixSqlSessionFactory(boolean jdbcBatchProcessing, ProcessEngineConfigurationImpl processEngineConfiguration) {
        super(jdbcBatchProcessing);
        this.dataBaseMetaData = new DataBaseMetaData(processEngineConfiguration.getDataSource(), "Camunda");
        this.processEngineConfiguration = processEngineConfiguration;
        setDatabaseType(processEngineConfiguration.getDatabaseType());
        setIdGenerator(processEngineConfiguration.getIdGenerator());
        setSqlSessionFactory(processEngineConfiguration.getSqlSessionFactory());
        setDbIdentityUsed(processEngineConfiguration.isDbIdentityUsed());
        setDbHistoryUsed(processEngineConfiguration.isDbHistoryUsed());
        setCmmnEnabled(processEngineConfiguration.isCmmnEnabled());
        setDmnEnabled(processEngineConfiguration.isDmnEnabled());
        setDatabaseTablePrefix(processEngineConfiguration.getDatabaseTablePrefix());

        String databaseTablePrefix = processEngineConfiguration.getDatabaseTablePrefix();
        String databaseSchema = processEngineConfiguration.getDatabaseSchema();
        //hack for the case when schema is defined via databaseTablePrefix parameter and not via databaseSchema parameter
        if (databaseTablePrefix != null && databaseSchema == null && databaseTablePrefix.contains(".")) {
            databaseSchema = databaseTablePrefix.split("\\.")[0];
        }
        setDatabaseSchema(databaseSchema);
    }

    @Override
    public Session openSession() {
        //这里的schema和catalog有问题
        try {
            Connection connection = processEngineConfiguration.getDataSource().getConnection();
            return super.openSession(connection, dataBaseMetaData.getCatalog(), Optional.ofNullable(dataBaseMetaData.getSchema()).orElse(databaseSchema));
        } catch (SQLException e) {
            logger.warn("尝试打开数据源失败，使用默认打开session方法", e);
        }
        return super.openSession();
    }
}
