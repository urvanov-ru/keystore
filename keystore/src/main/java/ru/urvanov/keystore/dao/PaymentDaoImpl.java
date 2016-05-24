package ru.urvanov.keystore.dao;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import ru.urvanov.keystore.domain.Payment;
import ru.urvanov.keystore.domain.PaymentListItem;
import ru.urvanov.keystore.domain.PaymentListParameters;

@Repository("paymentDao")
public class PaymentDaoImpl implements PaymentDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Payment findById(Long id) {
        return em.find(Payment.class, id);
    }

    @Override
    public Payment getReference(Long id) {
        return em.getReference(Payment.class, id);
    }

    @Override
    public void save(Payment payment) {
        if (payment.getId() == null) {
            em.persist(payment);
        } else {
            em.merge(payment);
        }
    }

    @Override
    public List<PaymentListItem> list(PaymentListParameters paymentListParam) {
        StoredProcedureCall spc = new StoredProcedureCall();
        spc.setProcedureName("list_payment");
        spc.addParameter("bigint", paymentListParam.getUserId());
        spc.addParameter("character varying", paymentListParam.getClientName());
        spc.addParameter("character varying", paymentListParam.getPaymentType() == null ? null : paymentListParam.getPaymentType().name());
        spc.addParameter("character varying", paymentListParam.getStatus() == null ? null : paymentListParam.getStatus().name());
        spc.addParameter("character varying", paymentListParam.getMethod() == null ? null : paymentListParam.getMethod().name());
        spc.addParameter("date", paymentListParam.getCreatedAtBegin());
        spc.addParameter("date", paymentListParam.getCreatedAtEnd());
        spc.addParameter("bigint", paymentListParam.getOrderId());
        return spc.getResultList(em, PaymentListItem.class);
    }

    @Override
    public BigInteger countList(PaymentListParameters paymentListParam) {
        StoredProcedureCall spc = new StoredProcedureCall();
        spc.setProcedureName("list_payment");
        spc.addParameter("bigint", paymentListParam.getUserId());
        spc.addParameter("character varying", paymentListParam.getClientName());
        spc.addParameter("character varying", paymentListParam.getPaymentType() == null ? null : paymentListParam.getPaymentType().name());
        spc.addParameter("character varying", paymentListParam.getStatus() == null ? null : paymentListParam.getStatus().name());
        spc.addParameter("character varying", paymentListParam.getMethod() == null ? null : paymentListParam.getMethod().name());
        spc.addParameter("date", paymentListParam.getCreatedAtBegin());
        spc.addParameter("date", paymentListParam.getCreatedAtEnd());
        spc.addParameter("bigint", paymentListParam.getOrderId());
        return spc.getCount(em,  PaymentListItem.class);
    }

}
