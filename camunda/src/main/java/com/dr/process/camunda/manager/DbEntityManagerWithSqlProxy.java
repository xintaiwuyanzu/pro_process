package com.dr.process.camunda.manager;

import com.dr.process.camunda.annotations.SqlProxy;
import org.camunda.bpm.engine.impl.cfg.IdGenerator;
import org.camunda.bpm.engine.impl.db.ListQueryParameterObject;
import org.camunda.bpm.engine.impl.db.PersistenceSession;
import org.camunda.bpm.engine.impl.db.entitymanager.DbEntityManager;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 代理{@link org.camunda.bpm.engine.impl.db.entitymanager.DbEntityManager}，通过
 * 拦截{@link com.dr.process.camunda.annotations.SqlProxy}实现自定义sql语句
 *
 * @author dr
 */
public class DbEntityManagerWithSqlProxy extends DbEntityManager {

    private Map<Class, ClassAnnotationHolder> classClassAnnotationHolderMap = Collections.synchronizedMap(new HashMap<>(64));

    public DbEntityManagerWithSqlProxy(IdGenerator idGenerator, PersistenceSession persistenceSession) {
        super(idGenerator, persistenceSession);
    }

    @Override
    public List selectList(String statement, ListQueryParameterObject parameter) {
        String proxySql = getProxySql(SqlProxy.METHOD_NAME_LIST, statement, parameter.getClass());
        return super.selectList(proxySql, parameter);
    }

    @Override
    public Object selectOne(String statement, Object parameter) {
        String proxySql = getProxySql(SqlProxy.METHOD_NAME_ONE, statement, parameter.getClass());
        return super.selectOne(proxySql, parameter);
    }

    /**
     * 获取代理的sql key
     *
     * @param methodName
     * @param originalSql
     * @param aClass
     * @return
     */
    private String getProxySql(String methodName, String originalSql, Class<?> aClass) {
        if (aClass.isAnnotationPresent(SqlProxy.class) || aClass.isAnnotationPresent(SqlProxy.SqlProxies.class)) {
            ClassAnnotationHolder holder = classClassAnnotationHolderMap.computeIfAbsent(aClass, ClassAnnotationHolder::new);
            return holder.getProxySql(methodName, originalSql);
        }
        return originalSql;
    }


    private static class ClassAnnotationHolder {

        Map<String, SqlProxy> sqlProxyMap;

        public ClassAnnotationHolder(Class aClass) {
            SqlProxy[] sqlProxies = (SqlProxy[]) aClass.getAnnotationsByType(SqlProxy.class);
            sqlProxyMap = new HashMap<>(sqlProxies.length);
            for (SqlProxy sqlProxy : sqlProxies) {
                String key = sqlProxy.methodName() + sqlProxy.originalSql();
                sqlProxyMap.put(key, sqlProxy);
            }
        }

        /**
         * @param methodName
         * @param originalSql
         * @return
         */
        public String getProxySql(String methodName, String originalSql) {
            SqlProxy proxy = sqlProxyMap.get(methodName + originalSql);
            if (proxy != null) {
                String proxySql = proxy.proxySql();
                if (StringUtils.isEmpty(proxySql)) {
                    proxySql = originalSql + SqlProxy.PROXY_SQL_SUFFIX;
                }
                return proxySql;
            }
            return originalSql;
        }
    }


}
