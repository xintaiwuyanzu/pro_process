package com.dr;

import com.dr.entity.TestEntity;
import com.dr.entity.TestEntity1Info;
import com.dr.entity.TestEntityInfo;
import com.dr.framework.core.orm.sql.support.SqlQuery;

public class SqlTest {
    public static void main(String[] args) {
        SqlQuery sqlQuery = SqlQuery.from(new TestEntity());
        sqlQuery.join(TestEntityInfo.ID, TestEntity1Info.NAME)
                .notLike(TestEntityInfo.ID, TestEntity1Info.NAME)
                .equal(TestEntityInfo.NAME, "aaaaa")
                .equal(TestEntityInfo.NAME, "aaaaa")
                .or()
                .equal(TestEntityInfo.NAME, "aaaaa")
                .equal(TestEntityInfo.NAME, "aaaaa")
                .equal(TestEntityInfo.ID, "bbbb")
                .equal(TestEntityInfo.ID, "bbbb");
        sqlQuery.orNew()
                .equal(TestEntityInfo.ID, "bbbb")
                .equal(TestEntityInfo.ID, "bbbb")
                .or()
                .equal(TestEntityInfo.ID, "bbbb")
                .and()
                .equal(TestEntityInfo.ID)
                .andNew()
                .equal(TestEntityInfo.NAME1, "bbbb")
                .equal(TestEntityInfo.ID)
                .orderBy(TestEntityInfo.ID);
        System.out.println(sqlQuery);
        System.out.println(args.getClass().isArray());
    }
}
