package com.dr.framework.core.orm.sql.support;

import com.dr.framework.core.orm.sql.Column;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

abstract class AbstractSqlQuery {
    Logger logger = LoggerFactory.getLogger(AbstractSqlQuery.class);

    static final String AND_NEW = ")) \nAND ((";
    static final String OR_NEW = ")) \nOR ((";
    static final String AND = " ) AND (";
    static final String OR = " ) OR (";
    static final String AND_ = " AND ";

    static final List<String> concats = Arrays.asList(AND, AND_NEW, OR, OR_NEW);

    protected void sqlClause(StringBuilder builder, String keyword, List<String> parts, String open, String close, String conjunction) {
        if (parts != null && !parts.isEmpty()) {
            builder.append(keyword);
            builder.append(" ");
            builder.append(open);
            int partsSize = parts.size();
            String last = "________";
            for (int i = 0; i < partsSize; i++) {
                String part = parts.get(i);
                boolean ex = i > 0 && !(concats.contains(last) || concats.contains(part));
                if (ex) {
                    builder.append(conjunction);
                }
                builder.append(part);
                last = part;
            }
            builder.append(close);
        }
    }

    abstract String sql(TableAlias tableAlias, SqlQuery sqlQuery);


    String formatSql(Column column, TableAlias tableAlias) {
        StringBuilder stringBuilder = new StringBuilder();
        if (!StringUtils.isEmpty(column.getTable())) {
            stringBuilder.append(tableAlias.alias(column.getTable()));
            stringBuilder.append(".");
        }
        stringBuilder.append(column.getName());
        if (!StringUtils.isEmpty(column.getAlias())) {
            stringBuilder.append(" as ");
            stringBuilder.append(column.getAlias());
        }
        return stringBuilder.toString();
    }
}
