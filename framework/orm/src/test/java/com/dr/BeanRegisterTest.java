package com.dr;

import com.dr.framework.core.orm.support.mybatis.spring.boot.autoconfigure.EnableAutoMapper;
import com.dr.service.TestService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = "spring.liquibase.enabled=false", classes = BeanRegisterTest.Application.class)
public class BeanRegisterTest {
    @Autowired
    DataSource dataSource;
    @Autowired
    TestService testService;

    @Test
    public void test() {
        System.out.println(dataSource.getClass().getName());
    }

    @Test
    public void test1() throws Exception {
        System.out.println(dataSource.getClass().getName());
        testService.aaa();
    }

    @SpringBootApplication
    @EnableAutoMapper(databases = {@EnableAutoMapper.DataBase(name = "one"), @EnableAutoMapper.DataBase(name = "two")})
    public static class Application {
        @Component
        class B implements BeanPostProcessor {
            @Override
            public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
                System.out.println(beanName);
                System.out.println(bean.getClass().getName());
                return bean;
            }
        }
    }
}
