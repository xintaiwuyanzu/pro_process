package com.dr.framework.core.orm.support.mybatis.page;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.RowBounds;

class OracleDialect implements Dialect {
    @Override
    public String parseToPageSql(MappedStatement mappedStatement, String sql, RowBounds rowBounds) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT * FROM ( SELECT TMP.*, ROWNUM ROW_ID FROM ( ")
                .append(sql)
                .append(" ) TMP WHERE ROWNUM <=")
                .append((rowBounds.getOffset() >= 1) ? (rowBounds.getOffset() + rowBounds.getLimit()) : rowBounds.getLimit())
                .append(") WHERE ROW_ID > ")
                .append(rowBounds.getOffset());
        return builder.toString();
    }
}
