package com.dr.framework.common.service;

import com.dr.framework.common.dao.CommonMapper;
import com.dr.framework.common.entity.IdEntity;
import com.dr.framework.common.page.Page;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;

@Service
public class CommonService {
    @Autowired
    CommonMapper commonMapper;
    @Autowired(required = false)
    SecurityManager securityManager;

    @Transactional(rollbackFor = Exception.class)
    public <T extends IdEntity> void insert(T entity) {
        if (StringUtils.isEmpty(entity.getId())) {
            entity.setId(UUID.randomUUID().toString());
        }
        commonMapper.insert(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    public <T extends IdEntity> void update(T entity) {
        commonMapper.updateById(entity);
    }

    /**
     * 根据sqlQuery查询数据
     *
     * @param sqlQuery
     * @return
     */
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public <E extends IdEntity> E selectOne(SqlQuery<E> sqlQuery) {
        return commonMapper.selectOneByQuery(sqlQuery);
    }

    /**
     * 根据sqlquery查询分页数据
     *
     * @param sqlQuery
     * @param pageIndex
     * @param pageSize
     * @param <E>
     * @return
     */
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public <E extends IdEntity> Page<E> selectPage(SqlQuery<E> sqlQuery, int pageIndex, int pageSize) {
        return commonMapper.selectPageByQuery(sqlQuery, pageIndex * pageSize, (pageIndex + 1) * pageSize);
    }

    /**
     * 根据sqlQuery查询列表数据
     *
     * @param eSqlQuery
     * @param <E>
     * @return
     */
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public <E extends IdEntity> List<E> selectList(SqlQuery<E> eSqlQuery) {
        return commonMapper.selectByQuery(eSqlQuery);
    }

    @Transactional(rollbackFor = Exception.class)
    public <E extends IdEntity> long delete(SqlQuery<E> sqlQuery) {
        return commonMapper.deleteByQuery(sqlQuery);
    }

    /**
     * 指定表的id的对象是否存在
     *
     * @param entityClass
     * @param id
     * @return
     */
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public boolean exists(Class entityClass, String id) {
        return commonMapper.exists(entityClass, id);
    }
    /**
     * 根据id查询表中的单条数据
     */
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public Object findById(Class entityClass, String id) {
        return commonMapper.selectById(entityClass,id);
    }

}
