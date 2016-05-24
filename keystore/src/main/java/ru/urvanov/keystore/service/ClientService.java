package ru.urvanov.keystore.service;

import java.math.BigInteger;
import java.util.List;

import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PreAuthorize;

import ru.urvanov.keystore.domain.Client;
import ru.urvanov.keystore.domain.ClientListItem;
import ru.urvanov.keystore.domain.ClientListParameters;
import ru.urvanov.keystore.exception.ClientNameNotUniqueException;
import ru.urvanov.keystore.exception.UserNameNotUniqueException;

public interface ClientService {
    @PreAuthorize("isAuthenticated() and hasRole('ROLE_SERVICE') and principal.hasAccess('SERVICE_ADMIN_ACCOUNT') and #id?.equals(principal.client.id)"
            + " or isAuthenticated() and hasRole('ROLE_SERVICE') and (principal.hasAccess('SERVICE_ADMIN_CLIENT') or principal.hasAccess('SERVICE_READ_CLIENT') or principal.hasAccess('SERVICE_NEW_CLIENT_NOTIFICATION') or principal.hasAccess('SERVICE_ADD_CLIENT') or principal.hasAccess('SERVICE_EXPORT_CLIENT')) and !#id?.equals(principal.client.id)"
            + " or isAuthenticated() and hasRole('ROLE_CLIENT') and principal.hasAccess('CLIENT_ADMIN_CLIENT_ACCOUNT') and #id?.equals(principal.client.id)")
    Client findById(Long id);

    @PreAuthorize("isAuthenticated() and hasRole('ROLE_SERVICE') and principal.hasAccess('SERVICE_ADMIN_ACCOUNT') and #id?.equals(principal.client.id)"
            + " or isAuthenticated() and hasRole('ROLE_SERVICE') and (principal.hasAccess('SERVICE_ADMIN_CLIENT') or principal.hasAccess('SERVICE_READ_CLIENT') or principal.hasAccess('SERVICE_NEW_CLIENT_NOTIFICATION') or principal.hasAccess('SERVICE_ADD_CLIENT') or principal.hasAccess('SERVICE_EXPORT_CLIENT')) and !#id?.equals(principal.client.id)"
            + " or isAuthenticated() and hasRole('ROLE_CLIENT') and principal.hasAccess('CLIENT_ADMIN_CLIENT_ACCOUNT') and #id?.equals(principal.client.id)")
    Client findFullById(Long id);

    @PreAuthorize("isAuthenticated() and hasRole('ROLE_SERVICE') and principal.hasAccess('SERVICE_ADMIN_ACCOUNT') and #id?.equals(principal.client.id)"
            + " or isAuthenticated() and hasRole('ROLE_SERVICE') and (principal.hasAccess('SERVICE_ADMIN_CLIENT') or principal.hasAccess('SERVICE_READ_CLIENT') or principal.hasAccess('SERVICE_NEW_CLIENT_NOTIFICATION') or principal.hasAccess('SERVICE_ADD_CLIENT') or principal.hasAccess('SERVICE_EXPORT_CLIENT')) and !#id?.equals(principal.client.id)"
            + " or isAuthenticated() and hasRole('ROLE_CLIENT') and principal.hasAccess('CLIENT_ADMIN_CLIENT_ACCOUNT') and #id?.equals(principal.client.id)")
    Client getReference(Long id);

    @PreAuthorize(""
            + "(principal == null or isAnonymous()) and #client.id == null "
            + " or isAuthenticated() and hasRole('ROLE_SERVICE') and principal.hasAccess('SERVICE_ADMIN_ACCOUNT') and #client?.id!=null and #client?.id?.equals(principal.client.id)"
            + " or isAuthenticated() and hasRole('ROLE_SERVICE') and principal.hasAccess('SERVICE_ADMIN_CLIENT') and #client?.id!=null and !(#client?.id?.equals(principal.client.id))"
            + " or isAuthenticated() and hasRole('ROLE_SERVICE') and principal.hasAccess('SERVICE_ADD_CLIENT') and #client?.id==null"
            + " or isAuthenticated() and hasRole('ROLE_CLIENT') and principal.hasAccess('CLIENT_ADMIN_CLIENT_ACCOUNT') and #client?.id!=null and #client?.id.equals(principal.client.id)")
    void save(@P("client") Client client) throws ClientNameNotUniqueException,
            UserNameNotUniqueException;

    @PreAuthorize("isAuthenticated() and hasRole('ROLE_SERVICE') and (principal.hasAccess('SERVICE_ADMIN_CLIENT') or principal.hasAccess('SERVICE_READ_CLIENT') or principal.hasAccess('SERVICE_NEW_CLIENT_NOTIFICATION') or principal.hasAccess('SERVICE_ADD_CLIENT') or principal.hasAccess('SERVICE_EXPORT_CLIENT'))")
    List<ClientListItem> list(ClientListParameters clientListParameters);

    @PreAuthorize("isAuthenticated() and hasRole('ROLE_SERVICE') and (principal.hasAccess('SERVICE_ADMIN_CLIENT') or principal.hasAccess('SERVICE_READ_CLIENT') or principal.hasAccess('SERVICE_NEW_CLIENT_NOTIFICATION') or principal.hasAccess('SERVICE_ADD_CLIENT') or principal.hasAccess('SERVICE_EXPORT_CLIENT'))")
    BigInteger countList(ClientListParameters clientListParameters);

    @PreAuthorize("isAuthenticated() and hasRole('ROLE_API')")
    Client findByUniqueId(String username);

    @PreAuthorize("isAuthenticated() and hasRole('ROLE_SERVICE') and principal.hasAccess('SERVICE_ADMIN_ACCOUNT') and #client?.id!=null and #client?.id?.equals(principal.client.id)"
            + " or isAuthenticated() and hasRole('ROLE_SERVICE') and principal.hasAccess('SERVICE_ADMIN_CLIENT') and #client?.id!=null and !(#client?.id?.equals(principal.client.id))"
            + " or isAuthenticated() and hasRole('ROLE_SERVICE') and principal.hasAccess('SERVICE_ADD_CLIENT') and #client?.id==null"
            + " or isAuthenticated() and hasRole('ROLE_CLIENT') and principal.hasAccess('CLIENT_ADMIN_CLIENT_ACCOUNT') and #client?.id!=null and #client?.id.equals(principal.client.id)"
            + " or isAnonymous() and #client.id == null")
    void save(@P("client") Client client, String userPassword)
            throws ClientNameNotUniqueException, UserNameNotUniqueException;

    @PreAuthorize("isAuthenticated() and hasRole('ROLE_SERVICE') and principal.hasAccess('SERVICE_ADMIN_CLIENT') and !#id?.equals(principal.client.id)")
    void blockById(Long id);

    @PreAuthorize("isAuthenticated() and hasRole('ROLE_SERVICE') and principal.hasAccess('SERVICE_ADMIN_CLIENT') and !#client?.id?.equals(principal.client.id)")
    void giveDictServiceType(Client client, Long... dictServiceTypeIds);

    @PreAuthorize("isAuthenticated() and hasRole('ROLE_SERVICE') and principal.hasAccess('SERVICE_ADMIN_CLIENT') and !#client?.id?.equals(principal.client.id)")
    void removeDictServiceType(Client client, Long... dictServiceTypeIds);

    Client findService();

    @PreAuthorize("isAuthenticated() and hasRole('ROLE_SERVICE') and principal.hasAccess('SERVICE_ADMIN_CLIENT') and !#client?.id?.equals(principal.client.id)")
    void giveDictAction(Client client, Long... dictActionIds);

    @PreAuthorize("isAuthenticated() and hasRole('ROLE_SERVICE') and principal.hasAccess('SERVICE_ADMIN_CLIENT') and !#client?.id?.equals(principal.client.id)")
    void removeDictAction(Client client, Long... array);

    @PreAuthorize("isAuthenticated() and hasRole('ROLE_CLIENT') and principal.hasAccess('CLIENT_ADMIN_CLIENT_ACCOUNT') and #client?.id != null and principal.client.id.equals(#client.id)")
    void changePassword(Client client, String newPassword);
    
}
