package com.dr.process.camunda.config;

import com.dr.framework.autoconfig.ApplicationAutoConfiguration;
import com.dr.framework.common.service.DataBaseService;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.orm.database.DataBaseMetaData;
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
import org.camunda.bpm.spring.boot.starter.property.CamundaBpmProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.util.Map;

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
    CamundaProcessEngineConfiguration elEngineConfiguration(ApplicationContext applicationContext
            , @Autowired(required = false) CurrentElResolver elResolver) {
        return new CamundaProcessEngineElConfiguration(applicationContext, elResolver);
    }

    @Bean
    ExpressionManager expressionManager(ProcessEngineConfigurationImpl processEngineConfiguration) {
        return processEngineConfiguration.getExpressionManager();
    }

    @Bean
    CommandContextFactory commandContextFactory(ProcessEngineConfigurationImpl processEngineConfiguration) {
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
    CustomEventListener customEventListener() {
        return new CustomEventListener();
    }

    /**
     * 配置数据源
     *
     * @param transactionManager
     * @param dataSourceMap
     * @param name
     * @param properties
     * @return
     */
    @Bean
    CamundaDatasourceConfiguration camundaDatasourceConfiguration(PlatformTransactionManager transactionManager,
                                                                  Map<String, DataSource> dataSourceMap,
                                                                  @Value("${" + CamundaBpmProperties.PREFIX + ".database.name:}") String name,
                                                                  CamundaBpmProperties properties,
                                                                  DataBaseService dataBaseService
    ) {
        Assert.notNull(transactionManager, "未启动事务管理器");
        Assert.isTrue(!dataSourceMap.isEmpty(), "未设置数据源");
        DataSource dataSource;
        DataBaseMetaData dataBaseMetaData;
        if (dataSourceMap.size() == 1) {
            dataSource = dataSourceMap.values().iterator().next();
            dataBaseMetaData = dataBaseService.getAllDatabases().get(0);
        } else {
            Assert.isTrue(!StringUtils.isEmpty(name), "检测到多个数据源，请使用：" + CamundaBpmProperties.PREFIX + ".database.name 声明流程引擎所属数据源！");
            dataSource = dataSourceMap.get(name);
            dataBaseMetaData = dataBaseService.getDataBaseMetaData(name);
            Assert.notNull(dataSource, "未检测到名称为：" + name + "的数据源！");
        }


        return new CamundaDbFixConfig(transactionManager, dataSource, properties, dataBaseMetaData);
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

}
