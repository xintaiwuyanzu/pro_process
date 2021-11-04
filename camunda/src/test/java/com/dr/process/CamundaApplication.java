package com.dr.process;

import com.dr.framework.core.orm.support.mybatis.spring.boot.autoconfigure.EnableAutoMapper;
import com.dr.framework.util.Constants;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = Constants.PACKAGE_NAME, scanBasePackageClasses = CamundaApplication.class)
@EnableAutoMapper(basePackages = "com.dr")
public class CamundaApplication {
    public static void main(String[] args) {
        SpringApplication.run(CamundaApplication.class);
    }
}
