package com.dr.framework.common.entity;

public interface CreateInfoEntity extends IdEntity {
    public long getCreateDate();

    public void setCreateDate(long createDate);

    public String getCreatePerson();

    public void setCreatePerson(String createPerson);

    public long getUpdateDate();

    public void setUpdateDate(long updateDate);

    public String getUpdatePerson();

    public void setUpdatePerson(String updatePerson);
}
