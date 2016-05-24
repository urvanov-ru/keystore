package ru.urvanov.keystore.service;

import org.springframework.security.access.prepost.PreAuthorize;

import ru.urvanov.keystore.domain.GlobalSettings;

public interface GlobalSettingsService {

    GlobalSettings read();

    @PreAuthorize("isAuthenticated() and hasRole('ROLE_SERVICE')")
    void save(GlobalSettings globalSettings);
}
