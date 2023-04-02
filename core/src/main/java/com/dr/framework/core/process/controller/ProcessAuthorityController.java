package com.dr.framework.core.process.controller;

import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.process.entity.ProcessAuthority;
import com.dr.framework.core.process.entity.ProcessAuthorityInfo;
import com.dr.framework.core.process.service.ProcessAuthorityService;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: caor
 * @Date: 2023-04-01 17:01
 * @Description:
 */
@RestController
@RequestMapping("/api/processAuthority")
public class ProcessAuthorityController extends BaseServiceController<ProcessAuthorityService, ProcessAuthority> {
    @Override
    protected SqlQuery<ProcessAuthority> buildPageQuery(HttpServletRequest httpServletRequest, ProcessAuthority processAuthority) {
        Assert.isTrue(StringUtils.hasText(processAuthority.getProcessDefinitionId()), "流程id不能为空！");
        return SqlQuery.from(ProcessAuthority.class)
                .equal(ProcessAuthorityInfo.PROCESSDEFINITIONID, processAuthority.getProcessDefinitionId());
    }

    /**
     * 根据流程id查询配置的角色并且属于当前登录人机构的人员
     *
     * @param processDefinitionId
     * @return
     */
    @RequestMapping("/getPersonByRoleAndCurOrg")
    public ResultEntity getPersonByRoleAndCurOrg(String processDefinitionId) {
        return ResultEntity.success(service.getPersonByRoleAndCurOrg(processDefinitionId));
    }
}
