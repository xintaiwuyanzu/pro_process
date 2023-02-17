package com.dr.process.camunda.manager;

import com.dr.framework.core.orm.database.DataBaseMetaData;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.db.sql.DbSqlSessionFactory;
import org.camunda.bpm.engine.impl.interceptor.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
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
    public static final String DMDBMS = "dm";//达梦数据库

    public static void init() {
        String defaultOrderBy = "order by ${internalOrderBy}";
        String defaultEscapeChar = "'\\'";
        String defaultDistinctCountBeforeStart = "select count(distinct";
        String defaultDistinctCountBeforeEnd = ")";
        String defaultDistinctCountAfterEnd = "";

        databaseSpecificLimitBeforeStatements.put(DMDBMS, "select * from ( select a.*, ROWNUM rnum from (");
        optimizeDatabaseSpecificLimitBeforeWithoutOffsetStatements.put(DMDBMS, "select * from ( select a.*, ROWNUM rnum from (");
        databaseSpecificLimitAfterStatements.put(DMDBMS, "  ) a where ROWNUM < #{lastRow}) where rnum  >= #{firstRow}");
        optimizeDatabaseSpecificLimitAfterWithoutOffsetStatements.put(DMDBMS, "  ) a where ROWNUM <= #{maxResults})");
        databaseSpecificLimitBeforeWithoutOffsetStatements.put(DMDBMS, "");
        databaseSpecificLimitAfterWithoutOffsetStatements.put(DMDBMS, "AND ROWNUM <= #{maxResults}");
        databaseSpecificInnerLimitAfterStatements.put(DMDBMS, databaseSpecificLimitAfterStatements.get(DMDBMS));
        databaseSpecificLimitBetweenStatements.put(DMDBMS, "");
        databaseSpecificLimitBetweenFilterStatements.put(DMDBMS, "");
        databaseSpecificLimitBetweenAcquisitionStatements.put(DMDBMS, "");
        databaseSpecificOrderByStatements.put(DMDBMS, defaultOrderBy);
        databaseSpecificLimitBeforeNativeQueryStatements.put(DMDBMS, "");
        databaseSpecificDistinct.put(DMDBMS, "distinct");
        databaseSpecificNumericCast.put(DMDBMS, "");

        databaseSpecificCountDistinctBeforeStart.put(DMDBMS, defaultDistinctCountBeforeStart);
        databaseSpecificCountDistinctBeforeEnd.put(DMDBMS, defaultDistinctCountBeforeEnd);
        databaseSpecificCountDistinctAfterEnd.put(DMDBMS, defaultDistinctCountAfterEnd);

        databaseSpecificEscapeChar.put(DMDBMS, defaultEscapeChar);

        databaseSpecificDummyTable.put(DMDBMS, "FROM DUAL");
        databaseSpecificBitAnd1.put(DMDBMS, "BITAND(");
        databaseSpecificBitAnd2.put(DMDBMS, ",");
        databaseSpecificBitAnd3.put(DMDBMS, ")");
        databaseSpecificDatepart1.put(DMDBMS, "to_number(to_char(");
        databaseSpecificDatepart2.put(DMDBMS, ",");
        databaseSpecificDatepart3.put(DMDBMS, "))");

        databaseSpecificTrueConstant.put(DMDBMS, "1");
        databaseSpecificFalseConstant.put(DMDBMS, "0");
        databaseSpecificIfNull.put(DMDBMS, "NVL");

        databaseSpecificDaysComparator.put(DMDBMS, "${date} <= #{currentTimestamp} - ${days}");

        databaseSpecificCollationForCaseSensitivity.put(DMDBMS, "");

        addDatabaseSpecificStatement(DMDBMS, "selectHistoricProcessInstanceDurationReport", "selectHistoricProcessInstanceDurationReport_oracle");
        addDatabaseSpecificStatement(DMDBMS, "selectHistoricTaskInstanceDurationReport", "selectHistoricTaskInstanceDurationReport_oracle");
        addDatabaseSpecificStatement(DMDBMS, "selectHistoricTaskInstanceCountByTaskNameReport", "selectHistoricTaskInstanceCountByTaskNameReport_oracle");
        addDatabaseSpecificStatement(DMDBMS, "selectFilterByQueryCriteria", "selectFilterByQueryCriteria_oracleDb2");
        addDatabaseSpecificStatement(DMDBMS, "selectHistoricProcessInstanceIdsForCleanup", "selectHistoricProcessInstanceIdsForCleanup_oracle");
        addDatabaseSpecificStatement(DMDBMS, "selectHistoricDecisionInstanceIdsForCleanup", "selectHistoricDecisionInstanceIdsForCleanup_oracle");
        addDatabaseSpecificStatement(DMDBMS, "selectHistoricCaseInstanceIdsForCleanup", "selectHistoricCaseInstanceIdsForCleanup_oracle");
        addDatabaseSpecificStatement(DMDBMS, "selectHistoricBatchIdsForCleanup", "selectHistoricBatchIdsForCleanup_oracle");

        addDatabaseSpecificStatement(DMDBMS, "deleteAttachmentsByRemovalTime", "deleteAttachmentsByRemovalTime_oracle");
        addDatabaseSpecificStatement(DMDBMS, "deleteCommentsByRemovalTime", "deleteCommentsByRemovalTime_oracle");
        addDatabaseSpecificStatement(DMDBMS, "deleteHistoricActivityInstancesByRemovalTime", "deleteHistoricActivityInstancesByRemovalTime_oracle");
        addDatabaseSpecificStatement(DMDBMS, "deleteHistoricDecisionInputInstancesByRemovalTime", "deleteHistoricDecisionInputInstancesByRemovalTime_oracle");
        addDatabaseSpecificStatement(DMDBMS, "deleteHistoricDecisionInstancesByRemovalTime", "deleteHistoricDecisionInstancesByRemovalTime_oracle");
        addDatabaseSpecificStatement(DMDBMS, "deleteHistoricDecisionOutputInstancesByRemovalTime", "deleteHistoricDecisionOutputInstancesByRemovalTime_oracle");
        addDatabaseSpecificStatement(DMDBMS, "deleteHistoricDetailsByRemovalTime", "deleteHistoricDetailsByRemovalTime_oracle");
        addDatabaseSpecificStatement(DMDBMS, "deleteExternalTaskLogByRemovalTime", "deleteExternalTaskLogByRemovalTime_oracle");
        addDatabaseSpecificStatement(DMDBMS, "deleteHistoricIdentityLinkLogByRemovalTime", "deleteHistoricIdentityLinkLogByRemovalTime_oracle");
        addDatabaseSpecificStatement(DMDBMS, "deleteHistoricIncidentsByRemovalTime", "deleteHistoricIncidentsByRemovalTime_oracle");
        addDatabaseSpecificStatement(DMDBMS, "deleteJobLogByRemovalTime", "deleteJobLogByRemovalTime_oracle");
        addDatabaseSpecificStatement(DMDBMS, "deleteHistoricProcessInstancesByRemovalTime", "deleteHistoricProcessInstancesByRemovalTime_oracle");
        addDatabaseSpecificStatement(DMDBMS, "deleteHistoricTaskInstancesByRemovalTime", "deleteHistoricTaskInstancesByRemovalTime_oracle");
        addDatabaseSpecificStatement(DMDBMS, "deleteHistoricVariableInstancesByRemovalTime", "deleteHistoricVariableInstancesByRemovalTime_oracle");
        addDatabaseSpecificStatement(DMDBMS, "deleteUserOperationLogByRemovalTime", "deleteUserOperationLogByRemovalTime_oracle");
        addDatabaseSpecificStatement(DMDBMS, "deleteByteArraysByRemovalTime", "deleteByteArraysByRemovalTime_oracle");
        addDatabaseSpecificStatement(DMDBMS, "deleteHistoricBatchesByRemovalTime", "deleteHistoricBatchesByRemovalTime_oracle");

        HashMap constants = new HashMap<String, String>();
        constants.put("constant.event", "cast('event' as nvarchar2(255))");
        constants.put("constant.op_message", "NEW_VALUE_ || '_|_' || PROPERTY_");
        constants.put("constant_for_update", "for update");
        constants.put("constant.datepart.quarter", "'Q'");
        constants.put("constant.datepart.month", "'MM'");
        constants.put("constant.datepart.minute", "'MI'");
        constants.put("constant.null.startTime", "null START_TIME_");
        constants.put("constant.varchar.cast", "'${key}'");
        constants.put("constant.integer.cast", "NULL");
        constants.put("constant.null.reporter", "NULL AS REPORTER_");
        dbSpecificConstants.put(DMDBMS, constants);
    }

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
