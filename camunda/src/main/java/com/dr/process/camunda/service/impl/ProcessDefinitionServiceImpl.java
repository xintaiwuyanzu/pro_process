package com.dr.process.camunda.service.impl;

import com.dr.framework.common.dao.CommonMapper;
import com.dr.framework.common.page.Page;
import com.dr.framework.core.process.bo.ProcessDefinition;
import com.dr.framework.core.util.Constants;
import com.dr.process.camunda.service.ProcessDefinitionService;
import com.dr.process.camunda.utils.BeanMapper;
import org.camunda.bpm.application.ProcessApplicationReference;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.repository.DeploymentBuilder;
import org.camunda.bpm.engine.repository.ProcessDefinitionQuery;
import org.camunda.bpm.engine.spring.SpringProcessEngineConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.util.List;
import java.util.function.Consumer;

/**
 * 流程定义service
 *
 * @author dr
 */
@Service
public class ProcessDefinitionServiceImpl implements ProcessDefinitionService {

    @Autowired
    protected RepositoryService repositoryService;
    @Autowired(required = false)
    protected ProcessApplicationReference processApplicationReference;

    @Autowired(required = false)
    protected SpringProcessEngineConfiguration springProcessEngineConfiguration;

    @Autowired
    protected CommonMapper commonMapper;

    @Override
    @Transactional
    public List<ProcessDefinition> deploy(String type, String deployName, InputStream stream) {
        List<ProcessDefinition> processDefinitions = BeanMapper.newProcessDefinitionList(
                buildDeployment()
                        .addInputStream(deployName, stream)
                        .deployWithResult()
                        .getDeployedProcessDefinitions(),
                extendFixConsumer
        );
        if (!StringUtils.hasText(type)) {
            type = Constants.DEFAULT;
        }
        //添加类型
        for (ProcessDefinition processDefinition : processDefinitions) {
            ProcessDefinitionExtendEntity entity = new ProcessDefinitionExtendEntity();
            entity.setId(processDefinition.getId());
            entity.setType(type);
            if (commonMapper.exists(ProcessDefinitionExtendEntity.class, processDefinition.getId())) {
                commonMapper.updateIgnoreNullById(entity);
            } else {
                commonMapper.insert(entity);
            }
        }
        return processDefinitions;
    }


    @Override
    @Transactional
    public void deleteProcessByDefinitionId(String id) {
        //TODO 这里有级联删除流程实例和历史信息的参数
        repositoryService.deleteProcessDefinition(id);
        //TODO 删除关联表 有时间找一下listener实现全量删除
        commonMapper.deleteById(ProcessDefinitionExtendEntity.class, id);
    }

    @Override
    @Transactional
    public void deleteProcessByDefinitionKey(String defKey) {
        repositoryService.deleteProcessDefinitions()
                .byKey(defKey)
                .delete();
        //TODO 删除扩展表数据
    }

    /**
     * 根据主键查询
     *
     * @param id
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public ProcessDefinition selectById(String id) {
        List<ProcessDefinition> definitionList =
                BeanMapper.newProcessDefinitionList(
                        repositoryService.createProcessDefinitionQuery().
                                processDefinitionId(id).
                                list(),
                        extendFixConsumer
                );
        return definitionList.size() == 1 ? definitionList.get(0) : null;
    }

    @Override
    @Transactional(readOnly = true)
    public long count(ProcessDefinition processDefinition, boolean latestVersion) {
        return buildProcessDefinitionQuery(processDefinition, latestVersion).count();
    }


    @Override
    @Transactional(readOnly = true)
    public List<ProcessDefinition> selectList(ProcessDefinition processDefinition, boolean latestVersion) {
        return BeanMapper.newProcessDefinitionList(
                buildProcessDefinitionQuery(processDefinition, latestVersion)
                        .list(),
                extendFixConsumer
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProcessDefinition> selectPage(ProcessDefinition processDefinition, int pageIndex, int pageSize, boolean latestVersion) {
        ProcessDefinitionQuery query = buildProcessDefinitionQuery(processDefinition, latestVersion);
        return new Page<>(
                pageIndex,
                pageSize,
                query.count(),
                BeanMapper.newProcessDefinitionList(
                        query.listPage(pageIndex, pageSize),
                        extendFixConsumer
                )
        );
    }

    /**
     * 根据查询条件构造查询器
     *
     * @param processDefinition
     * @param latestVersion
     * @return
     */
    protected ProcessDefinitionQuery buildProcessDefinitionQuery(ProcessDefinition processDefinition, boolean latestVersion) {
        ProcessDefinitionQuery query = repositoryService.createProcessDefinitionQuery()
                //排序
                .orderByProcessDefinitionVersion()
                .desc()
                .orderByDeploymentTime()
                .desc();
        if (latestVersion) {
            query.latestVersion();
        }
        if (processDefinition != null) {
            //根据名称查询
            if (StringUtils.hasText(processDefinition.getName())) {
                query.processDefinitionNameLike(processDefinition.getName());
            }
        }
        //TODO 根据类型查询
        return query;
    }

    /**
     * 用来补全流程定义扩展信息
     */
    protected Consumer<ProcessDefinition> extendFixConsumer = def -> {
        if (StringUtils.isEmpty(def.getType()) && !StringUtils.isEmpty(def.getId())) {
            ProcessDefinitionExtendEntity extendEntity = commonMapper.selectById(ProcessDefinitionExtendEntity.class, def.getId());
            if (extendEntity != null) {
                def.setType(extendEntity.getType());
            } else {
                def.setType(Constants.DEFAULT);
            }
        }
    };

    protected DeploymentBuilder buildDeployment() {
        DeploymentBuilder builder = processApplicationReference == null ?
                repositoryService.createDeployment() :
                repositoryService.createDeployment(processApplicationReference);
        builder.name(DEFAULT_DEPLOY_NAME);
        if (springProcessEngineConfiguration != null) {
            builder.enableDuplicateFiltering(springProcessEngineConfiguration.isDeployChangedOnly());
        }
        return builder;
    }

}
