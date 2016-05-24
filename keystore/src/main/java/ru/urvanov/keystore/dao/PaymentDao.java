package ru.urvanov.keystore.dao;

import java.math.BigInteger;
import java.util.List;

import ru.urvanov.keystore.domain.Payment;
import ru.urvanov.keystore.domain.PaymentListItem;
import ru.urvanov.keystore.domain.PaymentListParameters;

public interface PaymentDao {
    Payment findById(Long id);

    Payment getReference(Long id);

    void save(Payment payment);

    List<PaymentListItem> list(PaymentListParameters paymentListParam);

    BigInteger countList(PaymentListParameters paymentListParam);
}
