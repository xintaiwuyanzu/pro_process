package com.dr.process.camunda.annotations;

import java.lang.annotation.*;

/**
 * 用来拦截{@link org.camunda.bpm.engine.impl.db.entitymanager.DbEntityManager}方法
 * 动态替换自定义sql语句
 *
 * @author dr
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(SqlProxy.SqlProxies.class)
public @interface SqlProxy {


    /**
     * 要拦截的方法名称
     *
     * @return
     */
    String methodName();

    /**
     * 原始sql语句{@link  org.apache.ibatis.session.Configuration#getMappedStatement(String)}
     *
     * @return
     */
    String originalSql();

    /**
     * 如果{@link #proxySql()}为空，
     * 则使用{@link #originalSql()}+{@link #PROXY_SQL_SUFFIX}的方式查找代理sql
     */
    String PROXY_SQL_SUFFIX = "custom";

    /**
     * 代理sql语句 {@link  org.apache.ibatis.session.Configuration#getMappedStatement(String)}
     *
     * @return
     */
    String proxySql() default "";

    /**
     * 可以重复使用注解
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface SqlProxies {
        SqlProxy[] value();
    }

}
