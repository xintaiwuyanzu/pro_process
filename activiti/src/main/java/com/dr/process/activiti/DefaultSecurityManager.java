package com.dr.process.activiti;

import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.security.SecurityHolder;
import org.activiti.api.runtime.shared.security.SecurityManager;

import java.util.List;

/**
 * 用户管理器
 *
 * @author dr
 */
public class DefaultSecurityManager implements SecurityManager {
    @Override
    public String getAuthenticatedUserId() {
        SecurityHolder securityHolder = SecurityHolder.get();
        Person person = securityHolder.currentPerson();
        if (person != null) {
            return person.getId();
        }
        return null;
    }

    @Override
    public List<String> getAuthenticatedUserGroups() throws SecurityException {
        return null;
    }

    @Override
    public List<String> getAuthenticatedUserRoles() throws SecurityException {
        return null;
    }
}
