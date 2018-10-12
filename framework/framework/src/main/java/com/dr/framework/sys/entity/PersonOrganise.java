package com.dr.framework.sys.entity;

import com.dr.framework.common.entity.BaseEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

@Table(name = "sys_person_organise", comment = "用户和机构的关联表", module = "sys")
public class PersonOrganise extends BaseEntity {
    @Column(name = "person_id", comment = "用户id", length = 100)
    private String personId;
    @Column(name = "organise_id", comment = "机构id", length = 100)
    private String organiseId;
    @Column(name = "is_default", comment = "是否默认的机构")
    private boolean isDefault;

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getOrganiseId() {
        return organiseId;
    }

    public void setOrganiseId(String organiseId) {
        this.organiseId = organiseId;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }
}
