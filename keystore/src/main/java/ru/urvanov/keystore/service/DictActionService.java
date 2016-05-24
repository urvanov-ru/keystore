package ru.urvanov.keystore.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

import ru.urvanov.keystore.domain.DictAction;

public interface DictActionService {
    @PreAuthorize("isAuthenticated() and hasRole('ROLE_SERVICE') and (principal.hasAccess('SERVICE_READ_ACTION') or principal.hasAccess('SERVICE_WRITE_ACTION') or principal.hasAccess('SERVICE_EXPORT_ACTION'))")
    DictAction findById(Long id);

    @PreAuthorize("isAuthenticated() and hasRole('ROLE_SERVICE') and (principal.hasAccess('SERVICE_READ_ACTION') or principal.hasAccess('SERVICE_WRITE_ACTION') or principal.hasAccess('SERVICE_EXPORT_ACTION'))")
    DictAction getReference(Long id);

    @PreAuthorize("isAuthenticated() and hasRole('ROLE_SERVICE') and principal.hasAccess('SERVICE_WRITE_ACTION')")
    void save(DictAction dictAction);

    @PreAuthorize("isAuthenticated() and hasRole('ROLE_SERVICE') and (principal.hasAccess('SERVICE_READ_ACTION') or principal.hasAccess('SERVICE_WRITE_ACTION') or principal.hasAccess('SERVICE_EXPORT_ACTION'))")
    List<DictAction> findAll();

    @PreAuthorize("isAuthenticated() and hasRole('ROLE_SERVICE') and principal.hasAccess('SERVICE_WRITE_ACTION')")
    void start(Long id, Date dateEnd) throws IOException;
}
