package com.dr.framework.core.security;

/**
 * 登陆失败错误
 */
public class UserLoginException extends RuntimeException {
    public UserLoginException() {
    }

    public UserLoginException(String message) {
        super(message);
    }

    public UserLoginException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserLoginException(Throwable cause) {
        super(cause);
    }

    public UserLoginException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
