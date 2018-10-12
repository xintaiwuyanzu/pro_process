package com.dr;

import com.dr.framework.core.orm.support.mybatis.spring.boot.autoconfigure.EnableAutoMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoMapper(databases = {@EnableAutoMapper.DataBase(name = "one"), @EnableAutoMapper.DataBase(name = "two")})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
