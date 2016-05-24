package ru.urvanov.keystore.service;

import org.springframework.security.access.prepost.PreAuthorize;

import ru.urvanov.keystore.domain.Client;
import ru.urvanov.keystore.domain.DictAction;
import ru.urvanov.keystore.domain.DictServiceType;
import ru.urvanov.keystore.domain.Order;
import ru.urvanov.keystore.domain.Payment;
import ru.urvanov.keystore.domain.User;

public interface MailService {

    @PreAuthorize("isAuthenticated() and hasRole('ROLE_SERVICE') and principal.hasAccess('SERVICE_SEND_NOTIFICATION')")
    void sendNotification(Client client, String title, String body);

    void sendNotificationNewClient(Client client, User user);

    void sendNotificationChangeClient(Client client);

    void sendNotificationBlockClient(Client client);

    void sendNotificationNewUser(User user);

    void sendNotificationChangeUser(User user);

    void sendNotificationBlockUser(User user);

    void sendNotificationNewOrder(Order order);

    void sendNotificationNewPayment(Payment payment);

    void sendNotificationPayBack(Payment payment);

    void sendNotificationCorrectingPayment(Payment payment);
    
    void sendNotificationGivenDictAction(Client client, DictAction dictAction);

    void sendNotificationGivenDictServiceType(Client client,
            DictServiceType dictServiceType);

    void sendNotificationBirthday();

    
}
