package com.dr.framework.core.orm.support.mybatis.page;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.RowBounds;

class SQLServerDialect implements Dialect {
    @Override
    public String parseToPageSql(MappedStatement mappedStatement, String sql, RowBounds rowBounds) {
        StringBuilder builder = new StringBuilder(sql);
        builder.append(" OFFSET ").append(rowBounds.getOffset()).append(" ROWS FETCH NEXT ");
        builder.append(rowBounds.getLimit()).append(" ROWS ONLY");
        return builder.toString();
    }
}
