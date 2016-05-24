package ru.urvanov.keystore.service;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;

import ru.urvanov.keystore.domain.User;
import ru.urvanov.keystore.domain.UserAccess;
import ru.urvanov.keystore.domain.UserAccessCode;
import ru.urvanov.keystore.domain.UserListItem;
import ru.urvanov.keystore.domain.UserListParameters;
import ru.urvanov.keystore.exception.InvalidActivationKeyException;
import ru.urvanov.keystore.exception.NoSuchUserException;
import ru.urvanov.keystore.exception.UserNameNotUniqueException;

public interface UserService {
    @PostAuthorize("returnObject == null"
            + " or isAuthenticated() and hasRole('ROLE_SERVICE') and principal.hasAccess('SERVICE_ADMIN_ACCOUNT') and returnObject?.client?.id.equals(principal.client.id)"
            + " or isAuthenticated() and hasRole('ROLE_SERVICE') and principal.hasAccess('SERVICE_ADMIN_CLIENT') and !returnObject?.client?.id.equals(principal.client.id)"
            + " or isAuthenticated() and hasRole('ROLE_CLIENT') and principal.hasAccess('CLIENT_ADMIN_CLIENT_ACCOUNT') and returnObject?.client?.id.equals(principal.client.id)"
            + " or isAuthenticated() and #id.equals(principal.user.id)")
    User findById(Long id);

    @PostAuthorize("returnObject == null"
            + " or isAuthenticated() and hasRole('ROLE_SERVICE') and principal.hasAccess('SERVICE_ADMIN_ACCOUNT') and returnObject?.client?.id.equals(principal.client.id)"
            + " or isAuthenticated() and hasRole('ROLE_SERVICE') and principal.hasAccess('SERVICE_ADMIN_CLIENT') and !returnObject?.client?.id.equals(principal.client.id)"
            + " or isAuthenticated() and hasRole('ROLE_CLIENT') and principal.hasAccess('CLIENT_ADMIN_CLIENT_ACCOUNT') and returnObject?.client?.id.equals(principal.client.id)"
            + " or isAuthenticated() and #id.equals(principal.user.id)")
    User findFullById(Long id);

    @PostAuthorize("returnObject == null"
            + " or isAuthenticated() and hasRole('ROLE_SERVICE') and principal.hasAccess('SERVICE_ADMIN_ACCOUNT') and returnObject?.client?.id.equals(principal.client.id)"
            + " or isAuthenticated() and hasRole('ROLE_SERVICE') and principal.hasAccess('SERVICE_ADMIN_CLIENT') and !returnObject?.client?.id.equals(principal.client.id)"
            + " or isAuthenticated() and hasRole('ROLE_CLIENT') and principal.hasAccess('CLIENT_ADMIN_CLIENT_ACCOUNT') and returnObject?.client?.id.equals(principal.client.id)"
            + " or isAuthenticated() and #id.equals(principal.user.id)")
    User getReference(Long id);

    @PostAuthorize("returnObject == null"
            + " or isAuthenticated() and hasRole('ROLE_SERVICE') and principal.hasAccess('SERVICE_ADMIN_ACCOUNT') and returnObject?.client?.id.equals(principal.client.id)"
            + " or isAuthenticated() and hasRole('ROLE_SERVICE') and principal.hasAccess('SERVICE_ADMIN_CLIENT') and !returnObject?.client?.id.equals(principal.client.id)"
            + " or isAuthenticated() and hasRole('ROLE_CLIENT') and principal.hasAccess('CLIENT_ADMIN_CLIENT_ACCOUNT') and returnObject?.client?.id.equals(principal.client.id)"
            + " or isAuthenticated() and #id.equals(principal.user.id)")
    User findByUserName(String userName);

    @PreAuthorize("isAnonymous() and #user.id == null "
            + " or isAuthenticated() and hasRole('ROLE_SERVICE') and principal.hasAccess('SERVICE_ADMIN_ACCOUNT') and #user.client.id.equals(principal.client.id)"
            + " or isAuthenticated() and hasRole('ROLE_SERVICE') and principal.hasAccess('SERVICE_ADMIN_CLIENT') and !#user.client.id.equals(principal.client.id)"
            + " or isAuthenticated() and hasRole('ROLE_SERVICE') and principal.hasAccess('SERVICE_ADD_CLIENT') and #user.id == null"
            + " or isAuthenticated() and hasRole('ROLE_CLIENT') and principal.hasAccess('CLIENT_ADMIN_CLIENT_ACCOUNT') and #user.client.id.equals(principal.client.id)"
            + " or isAuthenticated() and #user.id != null and #user.id.equals(principal.user.id)")
    void save(User user) throws UserNameNotUniqueException;

    @PostAuthorize("returnObject == null"
            + " or isAuthenticated() and hasRole('ROLE_SERVICE') and principal.hasAccess('SERVICE_ADMIN_ACCOUNT') and returnObject?.client?.id.equals(principal.client.id)"
            + " or isAuthenticated() and hasRole('ROLE_SERVICE') and principal.hasAccess('SERVICE_ADMIN_CLIENT') and !returnObject?.client?.id.equals(principal.client.id)"
            + " or isAuthenticated() and hasRole('ROLE_CLIENT') and principal.hasAccess('CLIENT_ADMIN_CLIENT_ACCOUNT') and returnObject?.client?.id.equals(principal.client.id)"
            + " or returnObject.id.equals(principal.user.id)")
    User findFullByUserName(String username);

    @PreAuthorize("isAuthenticated() and hasRole('ROLE_SERVICE') and (principal.hasAccess('SERVICE_ADMIN_ACCOUNT') or principal.hasAccess('SERVICE_ADMIN_CLIENT')) and #params.userId.equals(principal.user.id)"
            + " or isAuthenticated() and hasRole('ROLE_CLIENT') and principal.hasAccess('CLIENT_ADMIN_CLIENT_ACCOUNT') and #params.userId.equals(principal.user.id)")
    List<UserListItem> list(@P("params") UserListParameters params);

    @PreAuthorize("isAuthenticated() and hasRole('ROLE_SERVICE') and (principal.hasAccess('SERVICE_ADMIN_ACCOUNT') or principal.hasAccess('SERVICE_ADMIN_CLIENT')) and #params.userId.equals(principal.user.id)"
            + " or isAuthenticated() and hasRole('ROLE_CLIENT') and principal.hasAccess('CLIENT_ADMIN_CLIENT_ACCOUNT') and #params.userId.equals(principal.user.id)")
    BigInteger countList(@P("params") UserListParameters params);

    void sendActivationLetter(User user) throws UserNameNotUniqueException;

    @PreAuthorize("isAuthenticated() and hasRole('ROLE_SERVICE') and principal.hasAccess('SERVICE_ADMIN_CLIENT') and !#user.client.id.equals(principal.client.id)"
            + " or isAuthenticated() and hasRole('ROLE_SERVICE') and principal.hasAccess('SERVICE_ADMIN_ACCOUNT') and #user.client.id.equals(principal.client.id) and !#user.id.equals(principal.user.id)"
            + " or isAuthenticated() and hasRole('ROLE_CLIENT') and principal.hasAccess('CLIENT_ADMIN_CLIENT_ACCOUNT') and #user.client.id.equals(principal.client.id) and !#user.id.equals(principal.user.id)")
    void block(User user);

    @PreAuthorize("isAuthenticated() and hasRole('ROLE_SERVICE') and principal.hasAccess('SERVICE_ADMIN_CLIENT') and !#user.client.id.equals(principal.client.id)"
            + " or isAuthenticated() and hasRole('ROLE_SERVICE') and principal.hasAccess('SERVICE_ADMIN_ACCOUNT') and #user.client.id.equals(principal.client.id) and !#user.id.equals(principal.user.id)"
            + " or isAuthenticated() and hasRole('ROLE_CLIENT') and principal.hasAccess('CLIENT_ADMIN_CLIENT_ACCOUNT') and #user.client.id.equals(principal.client.id) and !#user.id.equals(principal.user.id)")
    Map<UserAccessCode, UserAccess> getUserAccesses(User user);
    
    void activate(Long id, String key) throws InvalidActivationKeyException;

    void sendPassword(String userName) throws NoSuchUserException;

    void changePassword(Long id, String newPassword, String key) throws NoSuchUserException;
}
