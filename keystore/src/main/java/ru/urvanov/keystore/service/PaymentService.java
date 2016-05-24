package ru.urvanov.keystore.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

import ru.urvanov.keystore.domain.Payment;
import ru.urvanov.keystore.domain.PaymentListItem;
import ru.urvanov.keystore.domain.PaymentListParameters;
import ru.urvanov.keystore.domain.PaymentType;

public interface PaymentService {
    Payment findById(Long id);

    Payment getReference(Long id);

    @PreAuthorize("isAuthenticated() and hasRole('ROLE_PAY')")
    void createPaymentForOrder(Long orderId, PaymentType paymentType);

    @PreAuthorize("isAuthenticated() and hasRole('ROLE_PAY') "
            + "or isAuthenticated() and hasRole('ROLE_SERVICE') and principal.hasAccess('SERVICE_READ_PAYMENT', 'SERVICE_EXECUTE_PAYMENT')")
    void createPaymentForOrder(Long orderId, PaymentType paymentPayment,
            BigDecimal amount, BigDecimal commission, String comment) throws IOException;
    
    @PreAuthorize("isAuthenticated() and hasRole('ROLE_SERVICE') and (principal.hasAccess('SERVICE_READ_PAYMENT','SERVICE_EXECUTE_PAYMENT','SERVICE_EXPORT_PAYMENT'))"
            + " or isAuthenticated() and hasRole('ROLE_CLIENT') and (principal.hasAccess('CLIENT_READ_PAYMENT', 'CLIENT_ADD_ORDER'))")
    List<PaymentListItem> list(PaymentListParameters paymentListParam);

    @PreAuthorize("isAuthenticated() and hasRole('ROLE_SERVICE') and (principal.hasAccess('SERVICE_READ_PAYMENT','SERVICE_EXECUTE_PAYMENT','SERVICE_EXPORT_PAYMENT'))"
            + " or isAuthenticated() and hasRole('ROLE_CLIENT') and (principal.hasAccess('CLIENT_READ_PAYMENT','CLIENT_ADD_ORDER'))")
    BigInteger countList(PaymentListParameters paymentListParam);

    @PreAuthorize("isAuthenticated() and hasRole('ROLE_SERVICE') and principal.hasAccess('SERVICE_READ_PAYMENT')")
    void createCorrectPayment(Long parentPaymentId, BigDecimal amount, BigDecimal commission, String comment) throws IOException;

    @PreAuthorize("isAuthenticated() and hasRole('ROLE_SERVICE') and principal.hasAccess('SERVICE_EXECUTE_PAYMENT')")
    void complete(Long paymentId);
}
