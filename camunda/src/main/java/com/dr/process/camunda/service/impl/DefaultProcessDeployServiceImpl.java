package com.dr.process.camunda.service.impl;

import com.dr.framework.common.dao.CommonMapper;
import com.dr.framework.core.process.bo.ProcessDefinition;
import com.dr.framework.core.util.Constants;
import com.dr.process.camunda.command.process.definition.extend.ProcessDefinitionExtendEntity;
import com.dr.process.camunda.service.ProcessDeployService;
import com.dr.process.camunda.utils.BeanMapper;
import org.camunda.bpm.application.ProcessApplicationReference;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.repository.DeploymentBuilder;
import org.camunda.bpm.engine.spring.SpringTransactionsProcessEngineConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.util.List;

/**
 * 流程定义service
 *
 * @author dr
 */
public class DefaultProcessDeployServiceImpl extends BaseProcessServiceImpl implements ProcessDeployService {

    private RepositoryService repositoryService;
    private ProcessApplicationReference processApplicationReference;
    private CommonMapper commonMapper;

    public DefaultProcessDeployServiceImpl(ProcessEngineConfigurationImpl processEngineConfiguration) {
        super(processEngineConfiguration);
    }

    @Override
    @Transactional
    public List<ProcessDefinition> deploy(String type, String deployName, InputStream stream) {
        List<ProcessDefinition> processDefinitions = BeanMapper.newProcessDefinitionList(
                buildDeployment()
                        .addInputStream(deployName, stream)
                        .deployWithResult()
                        .getDeployedProcessDefinitions()
        );
        if (!StringUtils.hasText(type)) {
            type = Constants.DEFAULT;
        }
        //添加类型
        for (ProcessDefinition processDefinition : processDefinitions) {
            ProcessDefinitionExtendEntity entity = new ProcessDefinitionExtendEntity();
            entity.setId(processDefinition.getId());
            entity.setType(type);
            if (getCommonMapper().exists(ProcessDefinitionExtendEntity.class, processDefinition.getId())) {
                getCommonMapper().updateIgnoreNullById(entity);
            } else {
                getCommonMapper().insert(entity);
            }
        }
        return processDefinitions;
    }


    @Override
    @Transactional
    public void deleteProcessByDefinitionId(String id) {
        //TODO 这里有级联删除流程实例和历史信息的参数
        getRepositoryService().deleteProcessDefinition(id);
        //TODO 删除关联表 有时间找一下listener实现全量删除
        getCommonMapper().deleteById(ProcessDefinitionExtendEntity.class, id);
    }

    @Override
    @Transactional
    public void deleteProcessByDefinitionKey(String defKey) {
        getRepositoryService().deleteProcessDefinitions()
                .byKey(defKey)
                .delete();
        //TODO 删除扩展表数据
    }

    protected DeploymentBuilder buildDeployment() {
        DeploymentBuilder builder = getProcessApplicationReference() == null ?
                getRepositoryService().createDeployment() :
                getRepositoryService().createDeployment(getProcessApplicationReference());
        builder.name(DEFAULT_DEPLOY_NAME);
        if (getProcessEngineConfiguration() != null && getProcessEngineConfiguration() instanceof SpringTransactionsProcessEngineConfiguration) {
            boolean filter = ((SpringTransactionsProcessEngineConfiguration) getProcessEngineConfiguration()).isDeployChangedOnly();
            builder.enableDuplicateFiltering(filter);
        }
        return builder;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        repositoryService = getProcessEngineConfiguration().getRepositoryService();
        try {
            processApplicationReference = getApplicationContext().getBean(ProcessApplicationReference.class);
        } catch (Exception ignore) {
        }
    }

    public CommonMapper getCommonMapper() {
        return commonMapper;
    }

    public ProcessApplicationReference getProcessApplicationReference() {
        return processApplicationReference;
    }

    public RepositoryService getRepositoryService() {
        return repositoryService;
    }
}
