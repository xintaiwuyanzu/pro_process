package com.dr.framework.core.process.service.impl;

import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.process.entity.OpinionManagement;
import com.dr.framework.core.process.entity.OpinionManagementInfo;
import com.dr.framework.core.process.service.OpinionManagementService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * 常用意见service
 *
 * @author wx
 * @author dr
 */
@Service
public class OpinionManagementServiceImpl extends DefaultBaseService<OpinionManagement> implements OpinionManagementService {
    @Override
    @Transactional(rollbackFor = Exception.class)
    public long insert(OpinionManagement entity) {
        Assert.isTrue(StringUtils.hasText(entity.getOpinion()), "意见内容不能为空！");
        long sameCount = count(SqlQuery.from(OpinionManagement.class)
                .equal(OpinionManagementInfo.CREATEPERSON, entity.getCreatePerson())
                .equal(OpinionManagementInfo.OPINION, entity.getOpinion())
        );
        Assert.isTrue(sameCount == 0, "存在重复意见");
        return super.insert(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public long updateById(OpinionManagement entity) {
        Assert.isTrue(StringUtils.hasText(entity.getOpinion()), "意见内容不能为空！");
        long sameCount = count(SqlQuery.from(OpinionManagement.class)
                .equal(OpinionManagementInfo.CREATEPERSON, entity.getCreatePerson())
                .equal(OpinionManagementInfo.OPINION, entity.getOpinion())
                .notEqual(OpinionManagementInfo.ID, entity.getId())
        );
        Assert.isTrue(sameCount == 0, "存在重复意见");
        return super.updateById(entity);
    }
}
