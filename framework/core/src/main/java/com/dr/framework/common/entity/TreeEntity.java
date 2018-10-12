package com.dr.framework.common.entity;

public interface TreeEntity<T> extends StatusEntity<T> {

    String getParentId();

    void setParentId(String parentId);

}
