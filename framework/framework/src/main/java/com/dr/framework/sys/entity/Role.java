package com.dr.framework.sys.entity;

import com.dr.framework.common.entity.BaseStatusEntity;

public class Role extends BaseStatusEntity<Integer> {
    private String code;
    private String description;
    private boolean isSys;
}
