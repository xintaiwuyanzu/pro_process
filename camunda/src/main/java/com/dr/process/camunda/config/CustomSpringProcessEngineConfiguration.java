package com.dr.process.camunda.config;

import com.dr.process.camunda.manager.DbFixSqlSessionFactory;
import org.camunda.bpm.engine.spring.SpringProcessEngineConfiguration;

/**
 * 自定义配置，能够支持达梦等国产数据库数据源
 *
 * @author dr
 */
public class CustomSpringProcessEngineConfiguration extends SpringProcessEngineConfiguration {
    static {
        databaseTypeMappings.setProperty("DM DBMS", "oracle");
        databaseTypeMappings.setProperty("DM DBMS", "dm"); //达梦
        DbFixSqlSessionFactory.init();
    }
}
