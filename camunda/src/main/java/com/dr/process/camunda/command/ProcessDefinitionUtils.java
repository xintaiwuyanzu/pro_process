package com.dr.process.camunda.command;

import com.dr.framework.core.process.bo.ProcessDefinition;
import com.dr.process.camunda.command.process.definition.extend.ProcessDefinitionEntityWithExtend;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * 流程定义转换工具类
 * 统一实现类的映射
 *
 * @author dr
 */
public final class ProcessDefinitionUtils {

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
        return sourceList != null ? sourceList.stream().map(p -> ProcessDefinitionUtils.newProcessDefinition(p, consumer)).collect(Collectors.toList()) : Collections.emptyList();
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
        //基本信息
        processDefinition.setName(source.getName());
        processDefinition.setDescription(source.getDescription());
        processDefinition.setId(source.getId());
        //专有信息
        processDefinition.setKey(source.getKey());
        processDefinition.setVersion(source.getVersion());
        if (source instanceof ProcessDefinitionEntityWithExtend) {
            processDefinition.setType(((ProcessDefinitionEntityWithExtend) source).getProcessType());
        }
        if (consumer != null) {
            consumer.accept(processDefinition);
        }
        return processDefinition;
    }


}
