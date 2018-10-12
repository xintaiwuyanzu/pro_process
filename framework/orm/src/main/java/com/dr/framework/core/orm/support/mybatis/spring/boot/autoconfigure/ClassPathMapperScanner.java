package com.dr.framework.core.orm.support.mybatis.spring.boot.autoconfigure;

import com.dr.framework.core.orm.support.mybatis.spring.mapper.MapperFactoryBean;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.util.Set;

public class ClassPathMapperScanner extends ClassPathBeanDefinitionScanner {
    public ClassPathMapperScanner(BeanDefinitionRegistry registry) {
        super(registry, false);
        addIncludeFilter(new AnnotationTypeFilter(Mapper.class));
        addIncludeFilter(new AnnotationTypeFilter(com.dr.framework.core.orm.annotations.Mapper.class));
        addExcludeFilter((metadataReader, metadataReaderFactory) -> {
            String className = metadataReader.getClassMetadata().getClassName();
            return className.endsWith("package-info");
        });
    }

    @Override
    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
        return super.doScan(basePackages);
    }

    @Override
    protected void postProcessBeanDefinition(AbstractBeanDefinition beanDefinition, String beanName) {
        super.postProcessBeanDefinition(beanDefinition, beanName);
        String mapperInterface = beanDefinition.getBeanClassName();
        beanDefinition.setBeanClass(MapperFactoryBean.class);
        beanDefinition.getPropertyValues().add("mapperInterface", mapperInterface);
    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        AnnotationMetadata annotationMetadata = beanDefinition.getMetadata();
        return annotationMetadata.isInterface() && annotationMetadata.isIndependent();
    }
}
