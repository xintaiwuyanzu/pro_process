package com.dr.framework.common.entity;

import com.dr.framework.core.orm.annotations.Column;

public class BaseOrderedEntity extends BaseCreateInfoEntity implements OrderEntity {
    @Column(name = "order_info", comment = "排序", order = 6)
    private int orderBy;

    @Override
    public int getOrder() {
        return orderBy;
    }

    @Override
    public void setOrder(int order) {
        this.orderBy = order;
    }
}
