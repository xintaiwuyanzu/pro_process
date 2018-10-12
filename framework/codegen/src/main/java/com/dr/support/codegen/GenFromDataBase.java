package com.dr.support.codegen;

import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.exception.DatabaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.resource.ResourceAccessor;
import liquibase.snapshot.*;

import java.sql.SQLException;
import java.util.List;

public class GenFromDataBase {
    private JdbcConfig jdbcConfig;
    private String targetDir;
    private String packageName;

    public void doGen() {
        try {
            ResourceAccessor resourceAccessor = new ClassLoaderResourceAccessor();
            Database database = DatabaseFactory.getInstance().openDatabase(jdbcConfig.getUrl(), jdbcConfig.getUserName(), jdbcConfig.getPassword(), null, null, null, null, resourceAccessor);
            JdbcDatabaseSnapshot jdbcDatabaseSnapshot = (JdbcDatabaseSnapshot) SnapshotGeneratorFactory.getInstance().createSnapshot(database.getDefaultSchema(), database, new SnapshotControl(database));
            JdbcDatabaseSnapshot.CachingDatabaseMetaData cachingDatabaseMetaData = jdbcDatabaseSnapshot.getMetaDataFromCache();
            List<CachedRow> cachedRows = cachingDatabaseMetaData.getTables(database.getDefaultCatalogName(), database.getDefaultSchemaName(), null);
            TableGener tableGener = new TableGener();
            for (CachedRow cachedRow : cachedRows) {
                tableGener.gen(packageName, targetDir, cachedRow);
            }
        } catch (DatabaseException e) {
            e.printStackTrace();
        } catch (InvalidExampleException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public JdbcConfig getJdbcConfig() {
        return jdbcConfig;
    }

    public void setJdbcConfig(JdbcConfig jdbcConfig) {
        this.jdbcConfig = jdbcConfig;
    }

    public String getTargetDir() {
        return targetDir;
    }

    public void setTargetDir(String targetDir) {
        this.targetDir = targetDir;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
