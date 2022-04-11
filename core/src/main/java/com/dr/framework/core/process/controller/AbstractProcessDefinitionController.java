package com.dr.framework.core.process.controller;

import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.common.entity.ResultListEntity;
import com.dr.framework.common.page.Page;
import com.dr.framework.core.organise.entity.Organise;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.organise.service.OrganisePersonService;
import com.dr.framework.core.process.bo.ProcessDefinition;
import com.dr.framework.core.process.query.ProcessDefinitionQuery;
import com.dr.framework.core.process.service.ProcessTypeProvider;
import com.dr.framework.core.process.service.impl.ProcessPermissionTypeProvider;
import com.dr.framework.core.process.vo.ProcessTypeProviderWrapper;
import com.dr.framework.core.security.bo.PermissionMatcher;
import com.dr.framework.core.security.service.SecurityManager;
import com.dr.framework.core.util.Constants;
import com.dr.framework.core.web.annotations.Current;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 暂时的流程管理统一入口
 *
 * @author dr
 */
public class AbstractProcessDefinitionController extends BaseProcessController implements InitializingBean {

    private List<ProcessTypeProvider> processTypeProviders = Collections.emptyList();
    @Autowired
    protected ProcessPermissionTypeProvider processPermissionTypeProvider;
    @Autowired
    protected SecurityManager securityManager;
    @Autowired
    protected OrganisePersonService organisePersonService;

    /**
     * 查询流程定义分页
     *
     * @return
     */
    @PostMapping("/page")
    public ResultEntity page(ProcessDefinitionQuery query, @RequestParam(defaultValue = "0") int pageIndex, @RequestParam(defaultValue = Page.DEFAULT_PAGE_SIZE + "") int pageSize, @RequestParam(defaultValue = "true") boolean page) {
        if (page) {
            return ResultEntity.success(getProcessDefinitionService().processDefinitionPage(query, pageIndex, pageSize));
        } else {
            return ResultEntity.success(getProcessDefinitionService().processDefinitionList(query));
        }
    }

    /**
     * 获取当前登录用户指定类型的流程定义
     *
     * @param processType
     * @return
     */
    @PostMapping("userProcessDefinition")
    public ResultListEntity<ProcessDefinition> userProcessDefinition(@Current Person person, String processType, boolean useLatestVersion) {
        Assert.isTrue(StringUtils.hasText(processType), "流程类型不能为空");
        List<PermissionMatcher> userPermissions = securityManager.userPermissions(person.getId(), processPermissionTypeProvider.getType(), Constants.DEFAULT);
        if (userPermissions.isEmpty()) {
            return ResultListEntity.success(Collections.emptyList());
        } else {
            List<ProcessDefinition> processDefinitions = getProcessDefinitionService().processDefinitionList(
                            new ProcessDefinitionQuery()
                                    .typeLike(processType)
                                    .useLatestVersion(useLatestVersion)
                                    .withStartUser()
                                    .withProperty()
                    ).stream()
                    .filter(p -> checkPermission(p, userPermissions))
                    .collect(Collectors.toList());
            return ResultListEntity.success(processDefinitions);
        }
    }

    /**
     * 获取当前登录人所属机构的所有人员
     *
     * @param organise
     * @return
     */
    @PostMapping("/currentOrganisePersons")
    public ResultListEntity<Person> currentOrganisePersons(@Current Organise organise) {
        return ResultListEntity.success(organisePersonService.getOrganiseDefaultPersons(organise.getId()));
    }

    /**
     * 系统内置支持的流程类型
     * 供前端下拉选择使用
     *
     * @return
     */
    @GetMapping("/processType")
    public ResultListEntity<ProcessTypeProvider> processTypes(@Current Person person) {
        return ResultListEntity.success(processTypeProviders.stream().filter(p -> checkRole(p, person)).collect(Collectors.toList()));
    }

    /**
     * 判断当前登录人是否有指定类型流程的权限
     *
     * @param typeProvider
     * @param person
     * @return
     */
    private boolean checkRole(ProcessTypeProvider typeProvider, Person person) {
        if (person == null) {
            return false;
        }
        if (StringUtils.hasText(typeProvider.getRoleCode())) {
            return securityManager.hasRole(person.getId(), typeProvider.getRoleCode().split(","));
        }
        return true;
    }

    @Override
    public void afterPropertiesSet() {
        processTypeProviders = getApplicationContext().getBeansOfType(ProcessTypeProvider.class).values().stream().map(ProcessTypeProviderWrapper::new).collect(Collectors.toList());
    }

    private boolean checkPermission(ProcessDefinition p, List<PermissionMatcher> rolePermissions) {
        for (PermissionMatcher holder : rolePermissions) {
            if (holder.match(p.getId())) {
                return true;
            }
        }
        return false;
    }
}
