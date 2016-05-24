package ru.urvanov.keystore.service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

import ru.urvanov.keystore.domain.DictEvent;
import ru.urvanov.keystore.domain.DictEventCode;


public interface DictEventService {

    DictEvent findById(Long id);

    DictEvent getReference(Long id);

    @PreAuthorize("isAuthenticated() and hasRole('ROLE_SERVICE') and principal.hasAccess('SERVICE_SETTING_EMAIL_TEMPLATE')")
    void save(DictEvent dictEvent);

    DictEvent findByCode(DictEventCode code);

    List<DictEvent> findAll();
}
