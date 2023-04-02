package com.dr.framework.core.process.service;

import com.dr.framework.common.service.BaseService;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.process.entity.ProcessAuthority;

import java.util.List;

/**
 * @Author: caor
 * @Date: 2023-04-01 17:00
 * @Description:
 */
public interface ProcessAuthorityService extends BaseService<ProcessAuthority> {
    List<Person> getPersonByRoleAndCurOrg(String processDefinitionId);
}
