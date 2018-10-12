package com.dr.framework.core.orm.support.mybatis.page;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 方言模块，用于处理不同数据库的分页查询语句
 */
public interface Dialect {
    Logger logger = LoggerFactory.getLogger(Dialect.class);
    Map<Configuration, Dialect> configDialectMap = new ConcurrentHashMap<>();

    static String pageSql(MappedStatement mappedStatement, String sql, RowBounds rowBounds) {
        Dialect dialect = getDialect(mappedStatement);
        if (dialect != null) {
            return dialect.parseToPageSql(mappedStatement, sql, rowBounds);
        }
        return sql;
    }

    static Dialect getDialect(MappedStatement mappedStatement) {
        Configuration configuration = mappedStatement.getConfiguration();
        if (!configDialectMap.containsKey(configuration)) {
            DataSource dataSource = configuration.getEnvironment().getDataSource();
            Dialect dialect = createDialect(dataSource);
            if (dialect != null) {
                configDialectMap.put(configuration, dialect);
            }
        }
        return configDialectMap.get(configuration);
    }

    /**
     * todo  金仓数据库还有其他的国产数据库
     *
     * @param dataSource 数据源
     * @return 返回指定数据源的方言类
     */
    static Dialect createDialect(DataSource dataSource) {
        Dialect dialect = null;
        Connection connection = null;
        String product = null;
        try {
            connection = dataSource.getConnection();
            DatabaseMetaData metaData = connection.getMetaData();
            product = metaData.getDatabaseProductName();
            int version = metaData.getDatabaseMajorVersion();
            switch (product) {
                case "MySQL":
                case "HSQL Database Engine":
                    //可能是MariaDb数据库
                    dialect = new MysqlDialect();
                    break;
                case "H2":
                case "SQLite":
                    dialect = new H2Dialect();
                    break;
                case "PostgreSQL":
                    //可能是瀚高数据库
                    dialect = new PostgreDialect();
                    break;
                case "Microsoft SQL Server":
                    dialect = new SQLServerDialect();
                    break;
                case "Oracle":
                    dialect = new OracleDialect();
                    break;
                default:
                    break;
            }

        } catch (Exception e) {
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                }
            }
        }
        Assert.notNull(dialect, "没找到【" + product + "】的分页处理类，请联系管理员添加！");
        return dialect;
    }

    /**
     * 将sql语句转换为物理分页
     *
     * @param mappedStatement mappedStatement
     * @param sql             SQL语句
     * @param rowBounds       分页参数
     * @return 返回解析后的sql语句
     */
    String parseToPageSql(MappedStatement mappedStatement, String sql, RowBounds rowBounds);

}
