package com.dr.framework.core.process.controller;

import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.process.entity.OpinionManagement;
import com.dr.framework.core.process.entity.OpinionManagementInfo;
import com.dr.framework.core.process.service.OpinionManagementService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author wx
 * 意见管理
 */
@RestController
@RequestMapping("/api/opinion")
public class OpinionManagementController extends BaseServiceController<OpinionManagementService, OpinionManagement> {

    @Override
    protected SqlQuery<OpinionManagement> buildPageQuery(HttpServletRequest request, OpinionManagement entity) {
        Person person = getUserLogin(request);
        SqlQuery<OpinionManagement> equal = SqlQuery.from(OpinionManagement.class)
                .equal(OpinionManagementInfo.BUSINESSID, entity.getBusinessId())
                .equal(OpinionManagementInfo.CREATEPERSON, person.getId())
                .like(OpinionManagementInfo.OPINION, entity.getOpinion())
                .orderByDesc(OpinionManagementInfo.UPDATEDATE);
        return equal;
    }

    @Override
    public ResultEntity<OpinionManagement> insert(HttpServletRequest request, OpinionManagement entity) {
        Person person = getUserLogin(request);
        entity.setCreatePerson(person.getId());
        entity.setCreateDate(System.currentTimeMillis());
        return super.insert(request, entity);
    }

}
