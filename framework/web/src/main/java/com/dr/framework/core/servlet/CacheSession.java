package com.dr.framework.core.servlet;


import org.springframework.session.Session;

import java.time.Duration;
import java.time.Instant;
import java.util.Set;

public class CacheSession implements Session {
    @Override
    public String getId() {
        return null;
    }

    @Override
    public String changeSessionId() {
        return null;
    }

    @Override
    public <T> T getAttribute(String attributeName) {
        return null;
    }

    @Override
    public Set<String> getAttributeNames() {
        return null;
    }

    @Override
    public void setAttribute(String attributeName, Object attributeValue) {

    }

    @Override
    public void removeAttribute(String attributeName) {

    }

    @Override
    public Instant getCreationTime() {
        return null;
    }

    @Override
    public void setLastAccessedTime(Instant lastAccessedTime) {

    }

    @Override
    public Instant getLastAccessedTime() {
        return null;
    }

    @Override
    public void setMaxInactiveInterval(Duration interval) {

    }

    @Override
    public Duration getMaxInactiveInterval() {
        return null;
    }

    @Override
    public boolean isExpired() {
        return false;
    }
}
