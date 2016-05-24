package ru.urvanov.keystore.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ru.urvanov.keystore.domain.DictEvent;
import ru.urvanov.keystore.domain.DictEventCode;
import ru.urvanov.keystore.domain.DictEvent_;

@Repository("dictEventDao")
public class DictEventDaoImpl implements DictEventDao {

    @PersistenceContext
    private EntityManager em;

    @Transactional(readOnly = true)
    @Override
    public DictEvent findById(Long id) {
        return em.find(DictEvent.class, id);
    }

    @Override
    public DictEvent getReference(Long id) {
        return em.getReference(DictEvent.class, id);
    }

    @Transactional
    @Override
    public void save(DictEvent dictEvent) {
        if (dictEvent.getId() == null) {
            em.persist(dictEvent);
        } else {
            em.merge(dictEvent);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public DictEvent findByCode(DictEventCode code) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<DictEvent> criteriaQuery = criteriaBuilder
                .createQuery(DictEvent.class);
        Root<DictEvent> rootDictEvent = criteriaQuery.from(DictEvent.class);
        criteriaQuery.where(criteriaBuilder.equal(
                rootDictEvent.get(DictEvent_.code), code));
        TypedQuery<DictEvent> typedQuery = em.createQuery(criteriaQuery);
        List<DictEvent> result = typedQuery.getResultList();
        if (result.size() >= 1) {
            return result.get(0);
        } else {
            return null;
        }

    }

    @Override
    @Transactional(readOnly = true)
    public List<DictEvent> findAll() {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<DictEvent> criteriaQuery = criteriaBuilder
                .createQuery(DictEvent.class);
        criteriaQuery.from(DictEvent.class);
        TypedQuery<DictEvent> typedQuery = em.createQuery(criteriaQuery);
        return typedQuery.getResultList();
    }
}
