package ru.urvanov.keystore.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.urvanov.keystore.dao.KeyDao;
import ru.urvanov.keystore.dao.OrderDao;
import ru.urvanov.keystore.domain.Key;
import ru.urvanov.keystore.domain.KeyStatus;
import ru.urvanov.keystore.domain.Order;
import ru.urvanov.keystore.domain.OrderListItem;
import ru.urvanov.keystore.domain.OrderListParameters;
import ru.urvanov.keystore.domain.OrderStatus;
import ru.urvanov.keystore.domain.Payment;
import ru.urvanov.keystore.domain.PaymentMethod;
import ru.urvanov.keystore.domain.PaymentStatus;
import ru.urvanov.keystore.domain.PaymentType;

@Service("orderService")
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private MailService mailService;
    
    @Autowired
    private KeyDao keyDao;

    @Transactional(readOnly = true)
    @Override
    public Order findById(Long id) {
        return orderDao.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public Order findFullById(Long id) {
        return orderDao.findFullById(id);
    }

    // @Override
    // public Order getReference(Long id) {
    // return orderDao.getReference(id);
    // }

    @Transactional
    @Override
    public void save(Order order) {
        boolean newOrder = order.getId() == null;
        orderDao.save(order);
        if (newOrder) mailService.sendNotificationNewOrder(order);
    }

    @Transactional
    @Override
    public List<OrderListItem> list(OrderListParameters orderListParam) {
        return orderDao.list(orderListParam);
    }

    @Transactional
    @Override
    public BigInteger countList(OrderListParameters orderListParam) {
        return orderDao.countList(orderListParam);
    }

    @Transactional
    @Override
    public BigDecimal calculateRemainder(Order order) {
        
        Order fullOrder = orderDao.findFullById(order.getId());
        BigDecimal amount = fullOrder.getDictServiceType().getAmount();
        BigDecimal amount30Days = fullOrder.getDictServiceType().getAmount30Days();
        BigDecimal usedAmount = BigDecimal.ZERO;
        for (Key key : fullOrder.getKeys()) {
            if (key.getStatus() == KeyStatus.ACTIVE || key.getStatus() == KeyStatus.EXPIRED) {
                usedAmount.add(amount30Days);
            }
        }
        BigDecimal remainder = amount.subtract(usedAmount);
        if (remainder.compareTo(BigDecimal.ZERO) < 0)
            return BigDecimal.ZERO;
        else
            return remainder;
    }

    @Transactional
    @Override
    public void cancelOrder(Order order) {
        if (order.getStatus() == OrderStatus.PENDING_PAYMENT) {
            order.setStatus(OrderStatus.CANCEL);
            orderDao.save(order);
        } else {
            throw new IllegalStateException("Can not cancel order in non PENDING_PAYMENT state.");
        }
    }
    
    @Override
    @Transactional
    public boolean checkPayAmountById(Long orderId, BigDecimal amount) {
        Order order = orderDao.findById(orderId);
        return order.getStatus().equals(
                OrderStatus.PENDING_PAYMENT)
                && order.getDictServiceType().getAmount()
                        .compareTo(amount) == 0;
    }

    @Override
    public void payBackOrder(Order order) {
        if (order.getStatus() == OrderStatus.PAYED) {
            Order fullOrder = orderDao.findFullById(order.getId());
            Payment payment = new Payment();
            payment.setCreatedAt(new Date());
            payment.setAmountWithoutCommission(calculateRemainder(fullOrder));
            payment.setAmountOfCommission(BigDecimal.ZERO);
            payment.setAmountWithCommission(payment.getAmountWithoutCommission().negate());
            payment.setStatus(PaymentStatus.SCHEDULED);
            payment.setMethod(PaymentMethod.CASH);
            payment.setPaymentType(PaymentType.PAY_BACK);
            payment.setOrder(fullOrder);
            fullOrder.getPayments().add(payment);
            for (Key key : fullOrder.getKeys()) {
                if (key.getStatus() == KeyStatus.CREATED)
                    key.setStatus(KeyStatus.CANCELED);
            }
            fullOrder.setStatus(OrderStatus.PAY_BACK);
            
            orderDao.save(fullOrder);
        } else {
            throw new IllegalStateException("Can't pay by order in non COMPLETED state.");
        }
    }

}
