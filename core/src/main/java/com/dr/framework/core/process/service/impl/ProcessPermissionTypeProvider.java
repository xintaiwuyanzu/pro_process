package com.dr.framework.core.process.service.impl;

import com.dr.framework.core.process.bo.ProcessDefinition;
import com.dr.framework.core.process.query.ProcessDefinitionQuery;
import com.dr.framework.core.process.service.ProcessDefinitionService;
import com.dr.framework.core.process.service.ProcessTypeProvider;
import com.dr.framework.core.process.vo.ProcessDefinitionPermissionResource;
import com.dr.framework.core.process.vo.ProcessTypePermissionResource;
import com.dr.framework.core.security.bo.PermissionResource;
import com.dr.framework.core.security.service.ResourceProvider;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 这里放着缓存信息，用来给平台权限使用
 *
 * @author dr
 */
@Component
public class ProcessPermissionTypeProvider extends ApplicationObjectSupport implements ResourceProvider, InitializingBean {
    @Autowired
    ProcessDefinitionService processDefinitionService;
    protected List<ProcessTypeProvider> processTypeProviders = Collections.emptyList();

    /**
     * 读取所有的资源类型
     *
     * @param groupId
     * @return
     */
    @Override
    public List<? extends PermissionResource> getResources(String groupId) {
        List<PermissionResource> resources = new ArrayList<>();
        for (ProcessTypeProvider processTypeProvider : processTypeProviders) {
            resources.add(new ProcessTypePermissionResource(processTypeProvider));
            for (ProcessDefinition processDefinition : processDefinitionService.processDefinitionList(
                    new ProcessDefinitionQuery().typeLike(processTypeProvider.getType()).useLatestVersion(false))
            ) {
                resources.add(new ProcessDefinitionPermissionResource(processDefinition));
            }
        }
        return resources;
    }

    /**
     * 根据流程定义Id查询流程定义资源
     *
     * @param resourceId
     * @return
     */
    @Override
    public PermissionResource getResource(String resourceId) {
        ProcessDefinition processDefinition = processDefinitionService.getProcessDefinitionById(resourceId);
        if (processDefinition != null) {
            return new ProcessDefinitionPermissionResource(processDefinition);
        }
        return null;
    }

    @Override
    public void afterPropertiesSet() {
        processTypeProviders = new ArrayList<>(getApplicationContext().getBeansOfType(ProcessTypeProvider.class).values());
    }

    @Override
    public String getType() {
        return "processDefinition";
    }

    @Override
    public String getName() {
        return "系统流程";
    }

}
