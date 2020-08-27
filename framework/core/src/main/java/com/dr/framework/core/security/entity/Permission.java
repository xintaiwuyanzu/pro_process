package com.dr.framework.core.security.entity;

import com.dr.framework.core.orm.annotations.Table;
import com.dr.framework.core.util.Constants;

/**
 * 权限
 *
 * @author dr
 */
@Table(name = Constants.SYS_TABLE_PREFIX + "permission"
        , comment = "权限"
        , module = Constants.SYS_MODULE_NAME
        , genInfo = false)
public class Permission extends BaseSecurityRelation {
}
