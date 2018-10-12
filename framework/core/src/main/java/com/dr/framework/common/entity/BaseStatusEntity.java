package com.dr.framework.common.entity;

import com.dr.framework.core.orm.annotations.Column;

public class BaseStatusEntity<T> extends BaseOrderedEntity implements StatusEntity<T> {
    @Column(name = "status_info", comment = "状态", length = 10, order = 7)
    private T status;

    @Override
    public T getStatus() {
        return status;
    }

    @Override
    public void setStatus(T status) {
        this.status = status;
    }
}
