package ru.urvanov.keystore.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.urvanov.keystore.dao.OrderDao;
import ru.urvanov.keystore.dao.PaymentDao;
import ru.urvanov.keystore.domain.DictServiceType;
import ru.urvanov.keystore.domain.Key;
import ru.urvanov.keystore.domain.KeyActivationMode;
import ru.urvanov.keystore.domain.KeyData;
import ru.urvanov.keystore.domain.KeyStatus;
import ru.urvanov.keystore.domain.Order;
import ru.urvanov.keystore.domain.OrderStatus;
import ru.urvanov.keystore.domain.Payment;
import ru.urvanov.keystore.domain.PaymentListItem;
import ru.urvanov.keystore.domain.PaymentListParameters;
import ru.urvanov.keystore.domain.PaymentMethod;
import ru.urvanov.keystore.domain.PaymentStatus;
import ru.urvanov.keystore.domain.PaymentType;

@Service("paymentService")
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentDao paymentDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private MailService mailService;
    
    @Autowired
    private KeyService keyService;

    @Transactional(readOnly = true)
    @Override
    public Payment findById(Long id) {
        return paymentDao.findById(id);
    }

    @Override
    public Payment getReference(Long id) {
        return paymentDao.getReference(id);
    }

    //@Transactional
    //@Override
    //public void save(Payment payment) {
    //    paymentDao.save(payment);
    //}

    @Transactional
    @Override
    public void createPaymentForOrder(Long orderId, PaymentType paymentType) {
//        Payment payment = new Payment();
//        payment.setCreatedAt(new Date());
//        payment.setMethod(PaymentMethod.PAYMENT_SYSTEM);
//        payment.setPaymentType(paymentType);
//        payment.setOrder(orderDao.getReference(orderId));
//        paymentDao.save(payment);
//        switch (paymentType) {
//        case ORDER_PAYMENT:
//            mailService.sendNotificationNewPayment(payment);
//            break;
//        case PAY_BACK:
//            mailService.sendNotificationPayBack(payment);
//            break;
//        case CORRECTING:
//            mailService.sendNotificationCorrectingPayment(payment);
//        }
        throw new IllegalStateException("Not implemented");
    }
    
    @Override
    public void createPaymentForOrder(Long orderId, PaymentType paymentType,
            BigDecimal amount, BigDecimal commission, String comment) throws IOException {
        Order order = orderDao.findFullById(orderId);
        if (order.getStatus() == OrderStatus.PENDING_PAYMENT) {
            order.setStatus(OrderStatus.PAYED);
            order.setPayDateTime(new Date());
        }
        order.setStatus(OrderStatus.PAYED);
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmountWithoutCommission(amount);
        payment.setAmountWithCommission(amount.add(commission));
        payment.setAmountOfCommission(commission);
        payment.setCreatedAt(new Date());
        payment.setMethod(PaymentMethod.PAYMENT_SYSTEM);
        payment.setPaymentType(paymentType);
        payment.setStatus(PaymentStatus.COMPLETED);
        payment.setComment(comment);
        order.getPayments().add(payment);
        orderDao.save(order);
        if (paymentType == PaymentType.ORDER_PAYMENT) {
            order = orderDao.findFullById(order.getId());
            DictServiceType dictServiceType = order.getDictServiceType();
            String baseKey = dictServiceType.getBaseKey();
            KeyData baseKeyDataEntity = new KeyData();// from base key
            long expired = 1_000_000_000L;// from baseKeyDataEntity
            long started = 0L; // from baseKeyDataEntity
            long difference = expired - started;
            long differenceDays = difference / 1000 / 60 / 60 / 24;
            long difference30Days = differenceDays / 30;
            if (differenceDays % 30 > 0) difference30Days++;
            for (long n = 0; n < difference30Days; n++) {
                Key key = new Key();
                key.setOrder(order);
                key.setStatus(KeyStatus.CREATED);
                key.setClient(order.getCreatedBy().getClient());
                order.getKeys().add(key);
                if (order.getKeyActivationMode() == KeyActivationMode.AUTOMATIC) {
                    keyService.activateAndSaveKey(key);
                } else {
                    keyService.save(key);
                }
            }
        }
        switch (paymentType) {
        case ORDER_PAYMENT:
            mailService.sendNotificationNewPayment(payment);
            break;
        case PAY_BACK:
            mailService.sendNotificationPayBack(payment);
            break;
        case CORRECTING:
            mailService.sendNotificationCorrectingPayment(payment);
        }
    }

    @Transactional
    @Override
    public List<PaymentListItem> list(PaymentListParameters paymentListParam) {
        return paymentDao.list(paymentListParam);
    }

    @Transactional
    @Override
    public BigInteger countList(PaymentListParameters paymentListParam) {
        return paymentDao.countList(paymentListParam);
    }

    @Transactional
    @Override
    public void createCorrectPayment(Long parentPaymentId,
            BigDecimal amount, BigDecimal commission, String comment) throws IOException {
        Payment parentPayment = paymentDao.findById(parentPaymentId);
        Order order = parentPayment.getOrder();
        this.createPaymentForOrder(order.getId(), PaymentType.CORRECTING, amount, commission, comment); 
    }

    @Transactional
    @Override
    public void complete(Long paymentId) {
        Payment payment = paymentDao.findById(paymentId);
        if (payment.getStatus() == PaymentStatus.SCHEDULED) {
            payment.setStatus(PaymentStatus.COMPLETED);
            paymentDao.save(payment);
        }
        else throw new IllegalStateException("Can't complete payment with non scheduled state.");
    }

}
