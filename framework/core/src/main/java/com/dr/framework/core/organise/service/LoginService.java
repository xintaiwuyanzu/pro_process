package com.dr.framework.core.organise.service;

import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.organise.entity.UserLogin;
import com.dr.framework.core.util.Constants;

/**
 * 登录接口类
 *
 * @author dr
 */
public interface LoginService {
    /**
     * 默认登录方式，对应人员编码{@link Person#userCode}
     */
    String LOGIN_TYPE_DEFAULT = "default";
    String LOGIN_TYPE_IDNO = "idno";
    String LOGIN_TYPE_PHONE = "phone";
    String LOGIN_TYPE_EMAIL = "email";
    String LOGIN_TYPE_QQ = "qq";
    String LOGIN_TYPE_WX = "wx";

    /**
     * 根据用户创建登录信息
     * <p>
     * 一个人员可以绑定多条登录信息
     * <p>
     * 默认绑定
     * 人员编码{@link Person#userCode}
     * 身份证{@link Person#idNo}
     * 电话{@link Person#phone}
     * 邮箱{@link Person#email}
     * QQ{@link Person#qq}111
     * WX{@link Person#weiChatId}
     * <p>
     * 自定义登录方式请使用
     *
     * @param person
     * @param password
     * @see #bingLogin(UserLogin)
     */
    void bindLogin(Person person, String password);

    /**
     * 绑定自定义登录账户
     *
     * @param personId
     * @param loginType
     * @param loginId
     * @param password
     */
    void addLogin(String personId, String loginType, String loginId, String password);

    /**
     * 登录验证，并返回人员基本信息
     *
     * @param loginId     登录账号
     * @param password    登录密码
     * @param loginType   登录类型
     * @param loginSource 登录ip
     * @param outUser     是否外部用户
     * @return
     */
    Person login(String loginId, String password, String loginType, String loginSource, boolean outUser);

    default Person login(String loginId, String password, String loginType, String loginSource) {
        return login(loginId, password, loginType, loginSource, false);
    }

    default Person login(String loginId, String password, String loginType) {
        return login(loginId, password, loginType, Constants.DEFAULT);
    }

    default Person login(String loginId, String password) {
        return login(loginId, password, LOGIN_TYPE_DEFAULT);
    }

    String auth(Person person);

    Person deAuth(String token);

    default String auth(String loginId, String password, String loginType, String loginSource) {
        return auth(login(loginId, password, loginType, loginSource));
    }

    default String auth(String loginId, String password, String loginType) {
        return auth(login(loginId, password, loginType));
    }

    default String auth(String loginId, String password) {
        return auth(login(loginId, password));
    }

    void changePassword(String personId, String newPassword);

    void freezeLogin(String personId);

    void unFreezeLogin(String personId);

    /**
     * 删除指定的登录账户
     *
     * @param loginId
     * @return
     */
    long removeLogin(String loginId);

    /**
     * 删除指定人员所有的登录账户
     *
     * @param personId
     * @return
     */
    long removePersonLogin(String personId);

}
