package com.dr.framework.core.orm.support.mybatis.spring.boot.autoconfigure;

import com.dr.framework.core.orm.annotations.Table;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 扫描所有包下面的entity类
 */
public class ClassPathEntityScanner extends ClassPathBeanDefinitionScanner {
    private List<String> includeModules = new ArrayList<>();
    private List<String> excludeModules = new ArrayList<>();

    public ClassPathEntityScanner(BeanDefinitionRegistry registry) {
        super(registry, false);
    }

    public List<String> scan(List<String> pkgs) {
        Set<BeanDefinitionHolder> definitionHolders = doScan(pkgs.toArray(new String[pkgs.size()]));
        return definitionHolders.stream().map(beanDefinitionHolder -> beanDefinitionHolder.getBeanDefinition().getBeanClassName()).collect(Collectors.toList());
    }

    public void registerFilters() {
        addIncludeFilter(new EntityModuleTypeFilter(includeModules));
        if (excludeModules.size() > 0) {
            addExcludeFilter(new EntityModuleTypeFilter(excludeModules));
        }
    }

    public void addModuleInclude(String... modules) {
        if (modules != null) {
            for (String module : modules) {
                includeModules.add(module.toLowerCase().trim());
            }
        }
    }

    public void addModuleExclude(String... modules) {
        if (modules != null) {
            for (String module : modules) {
                excludeModules.add(module.toLowerCase().trim());
            }
        }
    }

    @Override
    protected void registerBeanDefinition(BeanDefinitionHolder definitionHolder, BeanDefinitionRegistry registry) {
    }

    class EntityModuleTypeFilter implements TypeFilter {
        List<String> modules;

        public EntityModuleTypeFilter(List<String> modules) {
            this.modules = modules;
        }

        @Override
        public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) {
            AnnotationMetadata annotationMetadata = metadataReader.getAnnotationMetadata();
            String tableAnnName = Table.class.getName();
            if (annotationMetadata.hasAnnotation(tableAnnName)) {
                if (modules.size() > 0) {
                    Map<String, Object> tableAttrs = annotationMetadata.getAnnotationAttributes(tableAnnName);
                    Object moduleObj = tableAttrs.get("module");
                    if (!StringUtils.isEmpty(moduleObj)) {
                        return modules.contains(moduleObj.toString().toLowerCase().trim());
                    }
                } else {
                    return true;
                }
            }
            return false;
        }
    }
}
