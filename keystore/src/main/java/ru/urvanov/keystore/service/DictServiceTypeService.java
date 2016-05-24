package ru.urvanov.keystore.service;

import java.util.Collection;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

import ru.urvanov.keystore.domain.Client;
import ru.urvanov.keystore.domain.DictServiceType;

public interface DictServiceTypeService {
    @PreAuthorize("isAuthenticated() and hasRole('ROLE_SERVICE') or hasRole('ROLE_CLIENT')")
    DictServiceType findById(Long id);

    @PreAuthorize("isAuthenticated() and hasRole('ROLE_SERVICE') or hasRole('ROLE_CLIENT')")
    DictServiceType getReference(Long id);

    @PreAuthorize("isAuthenticated() and hasRole('ROLE_SERVICE')")
    List<DictServiceType> findAll();

    @PreAuthorize("isAuthenticated() and hasRole('ROLE_SERVICE') and principal.hasAccess('SERVICE_SETTING_DICT_SERVICE_TYPE')")
    void save(DictServiceType dictServiceType);

    @PreAuthorize("isAuthenticated() and hasRole('ROLE_SERVICE') and principal.hasAccess('SERVICE_SETTING_DICT_SERVICE_TYPE')")
    List<DictServiceType> findByName(String query, int start, int limit);

    @PreAuthorize("isAuthenticated() and hasRole('ROLE_SERVICE') or hasRole('ROLE_CLIENT')")
    Collection<DictServiceType> findByClient(Client fullClient);
}
