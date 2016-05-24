package ru.urvanov.keystore.dao;

import java.math.BigInteger;
import java.util.List;

import ru.urvanov.keystore.domain.Order;
import ru.urvanov.keystore.domain.OrderListItem;
import ru.urvanov.keystore.domain.OrderListParameters;

public interface OrderDao {
    Order findById(Long id);

    Order findFullById(Long id);

    Order getReference(Long id);

    void save(Order order);

    List<OrderListItem> list(OrderListParameters orderListParam);

    BigInteger countList(OrderListParameters orderListParam);
}
