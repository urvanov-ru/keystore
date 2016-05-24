package ru.urvanov.keystore.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ru.urvanov.keystore.domain.DictAction;

@Repository("dictActionDao")
public class DictActionDaoImpl implements DictActionDao {

    @PersistenceContext
    private EntityManager em;

    @Transactional(readOnly = true)
    @Override
    public DictAction findById(Long id) {
        return em.find(DictAction.class, id);
    }

    @Override
    public DictAction getReference(Long id) {
        return em.getReference(DictAction.class, id);
    }

    @Transactional
    @Override
    public void save(DictAction dictAction) {
        if (dictAction.getId() == null) {
            em.persist(dictAction);
        } else {
            em.merge(dictAction);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<DictAction> findAll() {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<DictAction> criteriaQuery = criteriaBuilder
                .createQuery(DictAction.class);
        criteriaQuery.from(DictAction.class);
        TypedQuery<DictAction> typedQuery = em.createQuery(criteriaQuery);
        return typedQuery.getResultList();
    }

}
