package com.dr.framework.sys.service;

import com.dr.framework.common.service.CommonService;
import com.dr.framework.sys.entity.UserLogin;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用来初始化数据库
 */
@Service
public class InitDataService implements InitializingBean {

    @Autowired
    CommonService commonService;

    @Override
    public void afterPropertiesSet() throws Exception {
        UserLogin userLogin = new UserLogin();
        userLogin.setLoginId("admin");
        userLogin.setId("admin");
        userLogin.setPassword("1234");
        if (!commonService.exists(UserLogin.class, userLogin.getId())) {
            commonService.insert(userLogin);
        }
    }
}
