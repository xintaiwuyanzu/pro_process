package com.dr.framework.core.process.entity;

import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;
import com.dr.framework.core.process.utils.Constants;

/**
 * @Author: caor
 * @Date: 2023-04-01 16:55
 * @Description: todo暂时实现流程绑定角色，未实现分环节
 */
@Table(name = Constants.TABLE_PREFIX + "PROCESS_AUTHORITY", comment = "流程权限管理", module = Constants.MODULE_NAME)
public class ProcessAuthority extends BaseStatusEntity<String> {
    @Column(comment = "流程id")
    private String processDefinitionId;


    @Column(comment = "角色id")
    private String roleId;

    @Column(comment = "角色编码")
    private String roleCode;

    @Column(comment = "角色名称")
    private String roleName;

    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    public void setProcessDefinitionId(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
