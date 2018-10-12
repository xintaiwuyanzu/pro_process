package com.dr.framework.core.orm.sql;

import org.apache.ibatis.type.JdbcType;

public class Column {
    public static Column distinct(Column column, String alias) {
        return column;
    }

    public static Column distinct(Column column) {
        return distinct(column, null);
    }

    public static Column count(Column column, String alias) {
        return column;
    }

    public static Column count(Column column) {
        return count(column, null);
    }

    public static Column sum(Column column, String alias) {
        return column;
    }

    public static Column sum(Column column) {
        return sum(column, null);
    }

    public static Column avg(Column column, String alias) {
        return column;
    }

    public static Column avg(Column column) {
        return avg(column, null);
    }

    public static Column max(Column column, String alias) {
        return column;
    }

    public static Column max(Column column) {
        return max(column, null);
    }

    public static Column min(Column column, String alias) {
        return column;
    }

    public static Column min(Column column) {
        return min(column, null);
    }

    /**
     * 表名
     */
    private String table;
    /**
     * 列名
     */
    private String name;
    /**
     * 别名，一般用作映射do的属性
     */
    private String alias;

    /**
     * 数据库类型
     */
    private JdbcType type;

    public Column(String name) {
        this.name = name;
    }

    public Column(String name, String alias) {
        this.name = name;
        this.alias = alias;
    }

    public Column(String table, String name, String alias) {
        this.table = table;
        this.name = name;
        this.alias = alias;
    }

    public Column(String table, String name, String alias, JdbcType type) {
        this.table = table;
        this.name = name;
        this.alias = alias;
        this.type = type;
    }


    public String getTable() {
        return table.trim();
    }

    public String getName() {
        return name;
    }

    public String getAlias() {
        return alias;
    }

    public JdbcType getType() {
        return type;
    }

    public void setType(JdbcType type) {
        this.type = type;
    }

}
