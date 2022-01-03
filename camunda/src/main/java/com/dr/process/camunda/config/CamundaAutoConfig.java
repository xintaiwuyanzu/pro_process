package com.dr.process.camunda.config;

import com.dr.framework.autoconfig.ApplicationAutoConfiguration;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.process.service.ProcessTypeProvider;
import com.dr.framework.core.process.service.impl.DefaultProcessTypeProvider;
import com.dr.framework.core.security.SecurityHolder;
import com.dr.framework.core.web.interceptor.PersonInterceptor;
import com.dr.process.camunda.resolver.CurrentElResolver;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.cfg.auth.ResourceAuthorizationProvider;
import org.camunda.bpm.engine.impl.el.ExpressionManager;
import org.camunda.bpm.engine.impl.interceptor.CommandContextFactory;
import org.camunda.bpm.spring.boot.starter.CamundaBpmAutoConfiguration;
import org.camunda.bpm.spring.boot.starter.configuration.CamundaDatasourceConfiguration;
import org.camunda.bpm.spring.boot.starter.configuration.CamundaProcessEngineConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 处理camunda自启动相关配置
 *
 * @author dr
 */
@Configuration
@AutoConfigureAfter(ApplicationAutoConfiguration.class)
@AutoConfigureBefore(CamundaBpmAutoConfiguration.class)
@ComponentScan("com.dr.process.camunda")
public class CamundaAutoConfig {

    @Bean
    CamundaProcessEngineConfiguration elEngineConfiguration(ApplicationContext applicationContext, @Autowired(required = false) CurrentElResolver elResolver) {
        return new CamundaProcessEngineElConfiguration(applicationContext, elResolver);
    }

    @Bean
    ExpressionManager expressionManager(ProcessEngineConfigurationImpl processEngineConfiguration) {
        return processEngineConfiguration.getExpressionManager();
    }

    @Bean
    CommandContextFactory processCommandContextFactory(ProcessEngineConfigurationImpl processEngineConfiguration) {
        return processEngineConfiguration.getCommandContextFactory();
    }


    /**
     * 注入组织机构权限相关
     *
     * @param resourceAuthorizationProvider
     * @return
     */
    // @Bean
    DefaultAuthorizationConfiguration authorizationConfiguration(ResourceAuthorizationProvider resourceAuthorizationProvider) {
        return new DefaultAuthorizationConfiguration(resourceAuthorizationProvider);
    }

    /**
     * 事件监听
     *
     * @return
     */
    @Bean
    ParserListenerPlugin customEventListener() {
        return new ParserListenerPlugin();
    }

    /**
     * camunda绑定指定的数据源
     *
     * @return
     */
    @Bean
    CamundaDataSourceBeanFactoryPostProcessor camundaDataSourceBeanFactoryPostProcessor() {
        return new CamundaDataSourceBeanFactoryPostProcessor();
    }

    /**
     * 配置数据源
     *
     * @return
     */
    @Bean
    CamundaDatasourceConfiguration camundaDatasourceConfiguration() {
        return new CamundaDbFixConfig();
    }

    /**
     * 拦截设置当前登录人员信息
     *
     * @param identityService
     * @return
     */
    @Bean
    public WebMvcConfigurer CamundaCurrentUser(IdentityService identityService) {
        return new WebMvcConfigurer() {
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(new HandlerInterceptor() {
                    @Override
                    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
                        Person person = (Person) request.getAttribute(SecurityHolder.CURRENT_PERSON_KEY);
                        if (person != null) {
                            identityService.setAuthenticatedUserId(person.getId());
                        }
                        return true;
                    }
                }).order(PersonInterceptor.ORDER + 1);
            }
        };
    }

    /**
     * 注入默认流程类型提供器，供前端选择使用
     *
     * @return
     */
    @Bean
    ProcessTypeProvider defaultProcessTypeProvider() {
        return new DefaultProcessTypeProvider();
    }

}
