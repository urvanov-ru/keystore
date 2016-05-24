package ru.urvanov.keystore.service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

import ru.urvanov.keystore.domain.DictClientGroup;

public interface DictClientGroupService {
    @PreAuthorize("isAuthenticated() and hasRole('ROLE_SERVICE')")
    DictClientGroup findById(Long id);

    @PreAuthorize("isAuthenticated() and hasRole('ROLE_SERVICE')")
    List<DictClientGroup> findAll();

    @PreAuthorize("isAuthenticated() and hasRole('ROLE_SERVICE') and principal.hasAccess('SERVICE_SETTING_CLIENT_GROUP')")
    void save(DictClientGroup dictClientGroup);

    DictClientGroup getReference(Long id);

    @PreAuthorize("isAuthenticated() and hasRole('ROLE_SERVICE')")
    List<DictClientGroup> findAllRoot();

    @PreAuthorize("isAuthenticated() and hasRole('ROLE_SERVICE')")
    List<DictClientGroup> findAllHierarchy();
}
