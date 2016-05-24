package ru.urvanov.keystore.service;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;

import ru.urvanov.keystore.domain.Key;
import ru.urvanov.keystore.domain.KeyData;
import ru.urvanov.keystore.domain.KeyListItem;
import ru.urvanov.keystore.domain.KeyListParameters;

public interface KeyService {
    @PreAuthorize("isAuthenticated() and hasRole('ROLE_API') or hasRole('ROLE_CLIENT')")
    @PostAuthorize("isAuthenticated() and hasRole('ROLE_API')"
            + " or isAuthenticated() and hasRole('ROLE_CLIENT') and (principal.hasAccess('CLIENT_READ_KEY') or principal.hasAccess('CLIENT_ACTIVATE_KEY') or principal.hasAccess('CLIENT_GET_KEY')) and returnObject.client.id.equals(principal.client.id)")
    Key findById(Long id);

    @PreAuthorize("isAuthenticated() and hasRole('ROLE_PAY') or "
            + "isAuthenticated() and #key.client.id.equals(principal.client.id)")
    void save(Key key);

    @PreAuthorize("isAuthenticated() and hasRole('ROLE_CLIENT') and #params!=null and #params.userId.equals(principal.user.id) and (principal.hasAccess('CLIENT_READ_KEY') or principal.hasAccess('CLIENT_ACTIVATE_KEY') or principal.hasAccess('CLIENT_GET_KEY')) ")
    List<KeyListItem> list(@P("params") KeyListParameters params);

    @PreAuthorize("isAuthenticated() and hasRole('ROLE_CLIENT') and (principal.hasAccess('CLIENT_READ_KEY') or principal.hasAccess('CLIENT_ACTIVATE_KEY') or principal.hasAccess('CLIENT_GET_KEY')) and #countParams.userId.equals(principal.user.id)")
    BigInteger countList(@P("countParams") KeyListParameters countParams);

    @PreAuthorize("isAuthenticated() and hasRole('ROLE_PAY') or "
            + "isAuthenticated() and hasRole('ROLE_CLIENT') and principal.hasAccess('CLIENT_ACTIVATE_KEY') and #key.client.id.equals(principal.client.id)")
    void activateAndSaveKey(Key key) throws IOException;

    String generateKey(KeyData keyData);

    @PreAuthorize("isAuthenticated() and hasRole('ROLE_API')")
    List<Key> findByClientId(Long clientId);

    @PreAuthorize("isAuthenticated() and hasRole('ROLE_CLIENT') and principal.hasAccess('CLIENT_ACTIVATE_KEY') and #key.client.id.equals(principal.client.id) and #key.dictAction != null and #key.status==T(ru.urvanov.keystore.domain.KeyStatus).CREATED")
    void delete(Key key);
}
