package com.dr.framework.core.process.service.impl;

import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.organise.entity.Organise;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.organise.service.OrganisePersonService;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.process.entity.ProcessAuthority;
import com.dr.framework.core.process.entity.ProcessAuthorityInfo;
import com.dr.framework.core.process.service.ProcessAuthorityService;
import com.dr.framework.core.security.SecurityHolder;
import com.dr.framework.core.security.entity.Role;
import com.dr.framework.sys.service.DefaultSecurityManager;
import com.dr.framework.sys.service.RoleService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * @Author: caor
 * @Date: 2023-04-01 17:01
 * @Description: TODO 删除流程时，应该删除对应的数据
 */
@Service
public class ProcessAuthorityServiceImpl extends DefaultBaseService<ProcessAuthority> implements ProcessAuthorityService {

    @Autowired
    RoleService roleService;
    @Autowired
    DefaultSecurityManager securityManager;
    @Autowired
    OrganisePersonService organisePersonService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public long insert(ProcessAuthority entity) {
        Assert.hasText(entity.getProcessDefinitionId(), "流程id不能为空！");
        String[] roleIds = entity.getRoleId().split(",");
        for (String roleId : roleIds) {
            Role role = roleService.selectById(roleId);
            entity.setRoleId(roleId);
            entity.setRoleCode(role.getCode());
            entity.setRoleName(role.getName());
            //判断存在
            boolean exists = commonMapper.existsByQuery(SqlQuery.from(ProcessAuthority.class)
                    .equal(ProcessAuthorityInfo.PROCESSDEFINITIONID, entity.getProcessDefinitionId())
                    .equal(ProcessAuthorityInfo.ROLEID, entity.getRoleId()));
            if (!exists) {
                super.insert(entity);
            }
        }
        return roleIds.length;
    }

    @Override
    public List<Person> getPersonByRoleAndCurOrg(String processDefinitionId) {
        if (StringUtils.hasText(processDefinitionId)) {
            Organise organise = SecurityHolder.get().currentOrganise();
            List<ProcessAuthority> list = selectList(SqlQuery.from(ProcessAuthority.class).equal(ProcessAuthorityInfo.PROCESSDEFINITIONID, processDefinitionId));
            List<Person> rolePersonList = new ArrayList<>();
            //根据角色编码返回人员信息
            List<Person> finalRolePersonList = rolePersonList;
            list.forEach(processAuthority -> finalRolePersonList.addAll(securityManager.roleUsers(processAuthority.getRoleId())));
            //去重
            rolePersonList = rolePersonList.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(Person::getId))), ArrayList::new));
            //获取当前登录机构下的人
            List<Person> currentPersons = organisePersonService.getOrganiseDefaultPersons(organise.getId());
            //取交集
            return rolePersonList.stream().filter(x -> currentPersons.stream().map(d -> d.getId()).collect(Collectors.toList()).contains(x.getId())).collect(Collectors.toList());
        } else return null;
    }
}
