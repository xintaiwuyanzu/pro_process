package com.dr.framework.core.orm.support.mybatis.page;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.RowBounds;

class H2Dialect implements Dialect {
    @Override
    public String parseToPageSql(MappedStatement mappedStatement, String sql, RowBounds rowBounds) {
        StringBuilder builder = new StringBuilder(sql);
        builder.append(" limit ").append(rowBounds.getLimit());
        builder.append(" offset ").append(rowBounds.getOffset());
        return builder.toString();
    }
}
