package com.dr.framework.core.servlet;

import org.springframework.session.SessionRepository;

public class CacheSessionRepository implements SessionRepository<CacheSession> {

    @Override
    public CacheSession createSession() {
        return null;
    }

    @Override
    public void save(CacheSession session) {

    }

    @Override
    public CacheSession findById(String id) {
        return null;
    }

    @Override
    public void deleteById(String id) {

    }
}
