package com.dr.process.camunda.command.process.definition;

import com.dr.framework.common.dao.CommonMapper;
import com.dr.framework.core.process.bo.Property;
import com.dr.process.camunda.command.process.definition.extend.ProcessDefinitionExtendEntity;
import com.dr.process.camunda.utils.BeanMapper;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.BaseElement;
import org.camunda.bpm.model.bpmn.instance.ExtensionElements;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaProperties;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.dr.framework.core.process.service.ProcessConstants.*;

/**
 * @author dr
 */
public abstract class AbstractProcessDefinitionCmd {
    private boolean withProperty;
    private boolean withStartUser;
    static final List<String> CUSTOM_KEYS = Arrays.asList(PROCESS_CREATE_PERSON_KEY, PROCESS_CREATE_NAME_KEY, CREATE_DATE_KEY,

            OWNER_KEY, OWNER_NAME_KEY,

            ASSIGNEE_KEY, ASSIGNEE_NAME_KEY,

            PROCESS_TITLE_KEY,

            PROCESS_FORM_URL_KEY);


    public AbstractProcessDefinitionCmd(boolean withProperty) {
        this.withProperty = withProperty;
    }

    public AbstractProcessDefinitionCmd(boolean withProperty, boolean withStartUser) {
        this.withProperty = withProperty;
        this.withStartUser = withStartUser;
    }

    protected com.dr.framework.core.process.bo.ProcessDefinition convertDefinition(org.camunda.bpm.engine.repository.ProcessDefinition processDefinition, CommandContext commandContext) {
        if (processDefinition == null) {
            return null;
        }
        com.dr.framework.core.process.bo.ProcessDefinition po = BeanMapper.
                newProcessDefinition(processDefinition, def -> {
                    if (StringUtils.isEmpty(def.getType()) && !StringUtils.isEmpty(def.getId())) {
                        CommonMapper commonMapper = getBean(commandContext, CommonMapper.class);
                        ProcessDefinitionExtendEntity extendEntity = commonMapper.selectById(ProcessDefinitionExtendEntity.class, def.getId());
                        if (extendEntity != null) {
                            def.setType(extendEntity.getType());
                        } else {
                            def.setType(DEFAULT_PROCESS_TYPE);
                        }
                    }
                });
        if (withProperty) {
            po.setProPerties(getProperty(processDefinition.getId(), processDefinition.getKey(), commandContext));
        }
        //TODO
        if (withStartUser) {
           /* BpmnModelInstance bpmnModelInstance = commandContext.getProcessEngineConfiguration().getRepositoryService().getBpmnModelInstance(processDefinition.getId());
            String candidateUsersString = bpmnModelInstance.getModelElementById(processDefinition.getKey()).getAttributeValueNs(CAMUNDA_BPMN_EXTENSIONS_NS.getNamespaceUri(), "candidateStarterUsers");
            if (!StringUtils.isEmpty(candidateUsersString)) {
                Expression expression = commandContext.getProcessEngineConfiguration()
                        .getExpressionManager()
                        .createExpression(candidateUsersString);
                Object o = expression.getValue(StartProcessVariableScope.getSharedInstance());
            }*/
        }
        return po;
    }

    //
    public static List<Property> getProperty(String processDefineId, String key, CommandContext commandContext) {
        BpmnModelInstance bpmnModelInstance = commandContext.getProcessEngineConfiguration().getRepositoryService().getBpmnModelInstance(processDefineId);
        BaseElement element = bpmnModelInstance.getModelElementById(key);
        ExtensionElements extensionElements = element.getExtensionElements();
        if (extensionElements != null) {
            return extensionElements.getElementsQuery().filterByType(CamundaProperties.class).list().stream().map(CamundaProperties::getCamundaProperties).reduce((l, cs) -> {
                l.addAll(cs);
                return l;
            }).get().stream().map(p -> {
                Property proPerty = new Property();
                proPerty.setId(p.getCamundaId());
                proPerty.setName(p.getCamundaName());
                proPerty.setValue(p.getCamundaValue());
                return proPerty;
            }).collect(Collectors.toList());
        }
        return null;
    }

    public static Map<String, Object> filter(Map<String, Object> source) {
        return source.entrySet().stream().filter(e -> !CUSTOM_KEYS.contains(e.getKey())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /**
     * 从spring上下文获取对象
     *
     * @param commandContext
     * @param beanClass
     * @param <T>
     * @return
     */
    public static <T> T getBean(CommandContext commandContext, Class<T> beanClass) {
        return commandContext.getProcessEngineConfiguration().getArtifactFactory().getArtifact(beanClass);
    }
}
