package com.dr.framework.core.process.query;

/**
 * 简单抽象父类
 *
 * @author dr
 */
public class AbsProcessQuery<T extends AbsProcessQuery> {
    /**
     * 是否查询定义的扩展属性
     */
    boolean withProperty = false;
    /**
     * 流程类型
     */
    private String type;

    public T withProperty() {
        this.withProperty = true;
        return (T) this;
    }

    public T typeLike(String type) {
        this.type = type;
        return (T) this;
    }

    public boolean isWithProperty() {
        return withProperty;
    }

    public void setWithProperty(boolean withProperty) {
        this.withProperty = withProperty;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
