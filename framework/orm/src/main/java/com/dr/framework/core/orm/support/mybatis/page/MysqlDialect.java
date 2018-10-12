package com.dr.framework.core.orm.support.mybatis.page;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.RowBounds;

class MysqlDialect implements Dialect {
    @Override
    public String parseToPageSql(MappedStatement mappedStatement, String sql, RowBounds rowBounds) {
        StringBuilder builder = new StringBuilder(sql);
        builder.append(" limit ").append(rowBounds.getOffset()).append(",").append(rowBounds.getLimit());
        return builder.toString();
    }
}
