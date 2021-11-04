package com.dr.process.camunda.utils;

import com.dr.framework.core.process.bo.ProcessDefinition;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * 统一实现类的映射
 *
 * @author dr
 */
public final class BeanMapper {

    /**
     * 创建list
     *
     * @param sourceList
     * @return
     */
    public static List<ProcessDefinition> newProcessDefinitionList(List<org.camunda.bpm.engine.repository.ProcessDefinition> sourceList) {
        return newProcessDefinitionList(sourceList, null);
    }

    /**
     * 创建list
     *
     * @param sourceList
     * @param consumer
     * @return
     */
    public static List<ProcessDefinition> newProcessDefinitionList(List<org.camunda.bpm.engine.repository.ProcessDefinition> sourceList, Consumer<ProcessDefinition> consumer) {
        return sourceList != null ? sourceList.stream()
                .map(p -> BeanMapper.newProcessDefinition(p, consumer))
                .collect(Collectors.toList()) : Collections.emptyList();
    }

    /**
     * 映射camunda到自定义ProcessDefinition
     *
     * @param source
     * @return
     */
    public static ProcessDefinition newProcessDefinition(org.camunda.bpm.engine.repository.ProcessDefinition source) {
        return newProcessDefinition(source, null);
    }

    /**
     * 映射camunda到自定义ProcessDefinition
     *
     * @param source
     * @param consumer 自定义额外的处理器
     * @return
     */
    public static ProcessDefinition newProcessDefinition(org.camunda.bpm.engine.repository.ProcessDefinition source, Consumer<ProcessDefinition> consumer) {
        ProcessDefinition processDefinition = new ProcessDefinition();

        processDefinition.setVersion(source.getVersion());
        processDefinition.setName(source.getName());
        processDefinition.setDescription(source.getDescription());
        processDefinition.setId(source.getId());
        processDefinition.setKey(source.getKey());

        if (consumer != null) {
            consumer.accept(processDefinition);
        }
        return processDefinition;
    }


}
