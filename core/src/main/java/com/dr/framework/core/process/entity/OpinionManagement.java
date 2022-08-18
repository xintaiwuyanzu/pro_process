package com.dr.framework.core.process.entity;

import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;
import com.dr.framework.core.process.utils.Constants;

/**
 * 意见管理
 * TODO 这里将来需要处理机构默认意见、环节默认意见等等场景
 *
 * @author wx
 */
@Table(name = Constants.TABLE_PREFIX + "OPINION_MANAGEMENT", comment = "意见管理", module = Constants.MODULE_NAME)
public class OpinionManagement extends BaseStatusEntity<String> {

    @Column(comment = "业务外键", length = 100)
    private String businessId;

    @Column(comment = "意见内容", length = 200)
    private String opinion;

    /**
     * 0是默认,1是个人的
     */
    @Column(comment = "默认数据", length = 50)
    private String defEnable;


    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

    public String getDefEnable() {
        return defEnable;
    }

    public void setDefEnable(String defEnable) {
        this.defEnable = defEnable;
    }
}
