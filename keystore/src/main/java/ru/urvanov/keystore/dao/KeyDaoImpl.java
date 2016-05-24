package ru.urvanov.keystore.dao;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ru.urvanov.keystore.domain.DictAction;
import ru.urvanov.keystore.domain.Key;
import ru.urvanov.keystore.domain.KeyListItem;
import ru.urvanov.keystore.domain.KeyListParameters;
import ru.urvanov.keystore.domain.Client_;
import ru.urvanov.keystore.domain.DictAction_;
import ru.urvanov.keystore.domain.Key_;

@Repository("keyDao")
public class KeyDaoImpl implements KeyDao {

    @PersistenceContext
    private EntityManager em;

    @Transactional(readOnly = true)
    @Override
    public Key findById(Long id) {
        return em.find(Key.class, id);
    }

    @Override
    public Key getReference(Long id) {
        return em.getReference(Key.class, id);
    }

    @Transactional
    @Override
    public void save(Key key) {
        if (key.getId() == null) {
            em.persist(key);
        } else {
            em.merge(key);
        }
    }

    @Transactional
    @Override
    public List<KeyListItem> list(KeyListParameters param) {
        StoredProcedureCall storedProcedureCall = new StoredProcedureCall();
        storedProcedureCall.setProcedureName("list_key");
        storedProcedureCall.addParameter("bigint", param.getUserId());
        storedProcedureCall.addParameter("character varying",
                param.getClientName());
        storedProcedureCall.addParameter("character varying",
                param.getStatus() == null ? null : param.getStatus().name());
        storedProcedureCall.addParameter("date", param.getActiveOnDate());
        storedProcedureCall.addParameter("bigint", param.getOrderId());
        return storedProcedureCall.getResultList(em, KeyListItem.class);
    }

    @Transactional
    @Override
    public BigInteger countList(KeyListParameters param) {
        StoredProcedureCall storedProcedureCall = new StoredProcedureCall();
        storedProcedureCall.setProcedureName("list_key");
        storedProcedureCall.addParameter("bigint", param.getUserId());
        storedProcedureCall.addParameter("character varying",
                param.getClientName());
        storedProcedureCall.addParameter("character varying",
                param.getStatus() == null ? null : param.getStatus().name());
        storedProcedureCall.addParameter("date", param.getActiveOnDate());
        storedProcedureCall.addParameter("bigint", param.getOrderId());
        return storedProcedureCall.getCount(em, KeyListItem.class);
    }

    @Transactional
    @Override
    public List<Key> findKeysByDictAction(DictAction action) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Key> criteriaQuery = criteriaBuilder
                .createQuery(Key.class);
        Root<Key> rootKey = criteriaQuery.from(Key.class);
        criteriaQuery.where(criteriaBuilder.equal(rootKey.get(Key_.dictAction)
                .get(DictAction_.id), action.getId()));
        TypedQuery<Key> typedQuery = em.createQuery(criteriaQuery);
        return typedQuery.getResultList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Key> findByClientId(Long clientId) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Key> criteriaQuery = criteriaBuilder
                .createQuery(Key.class);
        Root<Key> rootKey = criteriaQuery.from(Key.class);
        criteriaQuery.where(criteriaBuilder.equal(
                rootKey.get(Key_.client).get(Client_.id), clientId));
        criteriaQuery.orderBy(criteriaBuilder.asc(rootKey.get(Key_.createdAt)));
        TypedQuery<Key> typedQuery = em.createQuery(criteriaQuery);
        return typedQuery.getResultList();
    }

    @Transactional
    @Override
    public void delete(Long id) {
        em.remove(em.find(Key.class, id));
    }
}
