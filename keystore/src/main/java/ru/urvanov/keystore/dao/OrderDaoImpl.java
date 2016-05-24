package ru.urvanov.keystore.dao;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ru.urvanov.keystore.domain.Order;
import ru.urvanov.keystore.domain.OrderListItem;
import ru.urvanov.keystore.domain.OrderListParameters;

@Repository("orderDao")
public class OrderDaoImpl implements OrderDao {

    private static final Logger logger = LoggerFactory.getLogger(OrderDaoImpl.class);
    
    @PersistenceContext
    private EntityManager em;

    @Transactional(readOnly = true)
    @Override
    public Order findById(Long id) {
        return em.find(Order.class, id);
    }

    @Transactional(readOnly = true)
    @Override
    public Order findFullById(Long id) {
        logger.debug("id={}.", id);
        Order order = em.find(Order.class, id);
        if (order != null) {
            logger.debug("order.payments.size={}.", order.getPayments().size());
            logger.debug("order.keys.size={}.", order.getKeys().size());
        }
        return order;
    }

    @Transactional(readOnly = true)
    @Override
    public Order getReference(Long id) {
        return em.getReference(Order.class, id);
    }

    @Transactional
    @Override
    public void save(Order order) {
        if (order.getId() == null) {
            em.persist(order);
        } else {
            em.merge(order);
        }
    }

    @Transactional
    @Override
    public List<OrderListItem> list(OrderListParameters orderListParam) {
        StoredProcedureCall spc = new StoredProcedureCall();
        spc.setProcedureName("list_order");
        spc.addParameter("bigint", orderListParam.getUserId());
        spc.addParameter("character varying", orderListParam.getClientName());
        spc.addParameter("character varying",
                orderListParam.getStatus() == null ? null : orderListParam
                        .getStatus().name());
        spc.addParameter("date", orderListParam.getCreatedAtBegin());
        spc.addParameter("date", orderListParam.getCreatedAtEnd());
        return spc.<OrderListItem> getResultList(em, OrderListItem.class);
    }

    @Transactional
    @Override
    public BigInteger countList(OrderListParameters orderListParam) {
        StoredProcedureCall spc = new StoredProcedureCall();
        spc.setProcedureName("list_order");
        spc.addParameter("bigint", orderListParam.getUserId());
        spc.addParameter("character varying", orderListParam.getClientName());
        spc.addParameter("character varying",
                orderListParam.getStatus() == null ? null : orderListParam
                        .getStatus().name());
        spc.addParameter("date", orderListParam.getCreatedAtBegin());
        spc.addParameter("date", orderListParam.getCreatedAtEnd());
        return spc.<OrderListItem> getCount(em, OrderListItem.class);
    }
}
