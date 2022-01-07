package com.dr.process.camunda.service.impl;

import com.dr.framework.common.dao.CommonMapper;
import com.dr.framework.core.process.bo.ProcessDefinition;
import com.dr.framework.core.process.service.ProcessConstants;
import com.dr.process.camunda.command.ProcessDefinitionUtils;
import com.dr.process.camunda.command.process.definition.extend.ProcessDefinitionExtendEntity;
import com.dr.process.camunda.service.ProcessDeployService;
import org.apache.commons.codec.digest.DigestUtils;
import org.camunda.bpm.application.ProcessApplicationReference;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.repository.DeploymentBuilder;
import org.camunda.bpm.engine.spring.SpringTransactionsProcessEngineConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * 流程定义service
 *
 * @author dr
 */
@Service
public class DefaultProcessDeployServiceImpl extends BaseProcessServiceImpl implements ProcessDeployService {
    @Autowired
    private RepositoryService repositoryService;
    private ProcessApplicationReference processApplicationReference;
    @Autowired
    private CommonMapper commonMapper;

    @Override
    @Transactional
    public List<ProcessDefinition> deploy(String type, String resourceName, InputStream stream) {
        if (!StringUtils.hasText(type)) {
            type = ProcessConstants.DEFAULT_PROCESS_TYPE;
        }
        if (!StringUtils.hasText(resourceName)) {
            if (!stream.markSupported()) {
                try {
                    stream = new ByteArrayInputStream(StreamUtils.copyToByteArray(stream));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //TODO 这里根据二进制hash判断文件是否部署过
            try {
                //流只能读一次，标记回滚点
                stream.mark(0);
                resourceName = "sha256:" + DigestUtils.sha256Hex(stream) + ".bpmn";
                stream.reset();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        List<ProcessDefinition> processDefinitions = ProcessDefinitionUtils.newProcessDefinitionList(buildDeployment().source(ProcessDeployService.DEFAULT_DEPLOY_NAME).addInputStream(resourceName, stream).deployWithResult().getDeployedProcessDefinitions());
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
    @Transactional(readOnly = true)
    public InputStream getDeployResourceById(String processDefinitionId) {
        Assert.isTrue(StringUtils.hasText(processDefinitionId), "流程定义Id不能为空");
        return getRepositoryService().getProcessModel(processDefinitionId);
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
        getRepositoryService().deleteProcessDefinitions().byKey(defKey).delete();
        //TODO 删除扩展表数据
    }

    protected DeploymentBuilder buildDeployment() {
        DeploymentBuilder builder = getProcessApplicationReference() == null ? getRepositoryService().createDeployment() : getRepositoryService().createDeployment(getProcessApplicationReference());
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
