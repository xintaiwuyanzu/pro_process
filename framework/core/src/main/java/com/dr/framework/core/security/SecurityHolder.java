package com.dr.framework.core.security;

final class SecurityHolder {
    private static final ThreadLocal<ClientInfo> contextHolder = new ThreadLocal<>();

    static ClientInfo get() {
        return contextHolder.get();
    }


}
