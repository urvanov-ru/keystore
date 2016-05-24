package ru.urvanov.keystore.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;

import ru.urvanov.keystore.domain.Order;
import ru.urvanov.keystore.domain.OrderListItem;
import ru.urvanov.keystore.domain.OrderListParameters;

public interface OrderService {
    @PostAuthorize("isAuthenticated() and hasRole('ROLE_SERVICE') and (principal.hasAccess('SERVICE_READ_ORDER') or principal.hasAccess('SERVICE_EXPORT_ORDER'))"
            + " or isAuthenticated() and hasRole('ROLE_CLIENT') and (principal.hasAccess('CLIENT_READ_ORDER') or principal.hasAccess('CLIENT_ADD_ORDER')) and returnObject.createdBy.client.id.equals(principal.client.id)")
    Order findById(Long id);

    @PostAuthorize("isAuthenticated() and hasRole('ROLE_SERVICE') and (principal.hasAccess('SERVICE_READ_ORDER') or principal.hasAccess('SERVICE_EXPORT_ORDER'))"
            + " or isAuthenticated() and hasRole('ROLE_CLIENT') and (principal.hasAccess('CLIENT_READ_ORDER') or principal.hasAccess('CLIENT_ADD_ORDER')) and returnObject.createdBy.client.id.equals(principal.client.id)")
    Order findFullById(Long id);

    @PreAuthorize("isAuthenticated() and hasRole('ROLE_CLIENT') and principal.hasAccess('CLIENT_ADD_ORDER') and #order.id==null and #order.createdBy.id.equals(principal.user.id)")
    void save(Order order);

    @PreAuthorize("isAuthenticated() and hasRole('ROLE_SERVICE') and (principal.hasAccess('SERVICE_READ_ORDER') or principal.hasAccess('SERVICE_EXPORT_ORDER'))"
            + " or isAuthenticated() and hasRole('ROLE_CLIENT') and (principal.hasAccess('CLIENT_READ_ORDER') or principal.hasAccess('CLIENT_ADD_ORDER'))")
    List<OrderListItem> list(OrderListParameters orderListParam);

    @PreAuthorize("isAuthenticated() and hasRole('ROLE_SERVICE') and (principal.hasAccess('SERVICE_READ_ORDER') or principal.hasAccess('SERVICE_EXPORT_ORDER'))"
            + " or isAuthenticated() and hasRole('ROLE_CLIENT') and (principal.hasAccess('CLIENT_READ_ORDER') or principal.hasAccess('CLIENT_ADD_ORDER'))")
    BigInteger countList(OrderListParameters orderListParam);

    @PostAuthorize("isAuthenticated() and hasRole('ROLE_SERVICE') and (principal.hasAccess('SERVICE_READ_ORDER') or principal.hasAccess('SERVICE_EXPORT_ORDER'))"
            + " or isAuthenticated() and hasRole('ROLE_CLIENT') and (principal.hasAccess('CLIENT_READ_ORDER') or principal.hasAccess('CLIENT_ADD_ORDER')) and #order.createdBy.client.id.equals(principal.client.id)")
    BigDecimal calculateRemainder(Order order);

    @PostAuthorize("isAuthenticated() and hasRole('ROLE_CLIENT') and principal.hasAccess('CLIENT_ADD_ORDER') and #order.createdBy.client.id.equals(principal.client.id)")
    void cancelOrder(Order order);

    boolean checkPayAmountById(Long orderId, BigDecimal amount);

    @PostAuthorize("isAuthenticated() and hasRole('ROLE_CLIENT') and principal.hasAccess('CLIENT_ADD_ORDER') and #order.createdBy.client.id.equals(principal.client.id)")
    void payBackOrder(Order order);
}
