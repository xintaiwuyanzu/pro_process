package com.dr.process;

import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.process.camunda.command.process.definition.extend.ProcessDefinitionExtendEntity;
import com.dr.process.camunda.command.process.definition.extend.ProcessDefinitionExtendEntityInfo;

public class SqlQueryTest {
    public static void main(String[] args) {
        SqlQuery<ProcessDefinitionExtendEntity> sqlQuery = SqlQuery.from(ProcessDefinitionExtendEntity.class);
        sqlQuery.equal(ProcessDefinitionExtendEntityInfo.TYPE, "aaa");
        System.out.println(sqlQuery.get("$where"));
    }
}
