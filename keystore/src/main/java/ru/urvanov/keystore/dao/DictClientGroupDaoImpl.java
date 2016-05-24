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

import ru.urvanov.keystore.domain.DictClientGroup;
import ru.urvanov.keystore.domain.DictClientGroup_;

@Repository("dictClientGroupDao")
public class DictClientGroupDaoImpl implements DictClientGroupDao {

    @PersistenceContext
    private EntityManager em;

    @Transactional(readOnly = true)
    @Override
    public DictClientGroup findById(Long id) {
        return em.find(DictClientGroup.class, id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<DictClientGroup> findAll() {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<DictClientGroup> criteriaQuery = criteriaBuilder
                .createQuery(DictClientGroup.class);
        criteriaQuery.from(DictClientGroup.class);
        TypedQuery<DictClientGroup> typedQuery = em.createQuery(criteriaQuery);
        return typedQuery.getResultList();
    }

    @Override
    @Transactional
    public void save(DictClientGroup dictClientGroup) {
        if (dictClientGroup.getId() == null) {
            em.persist(dictClientGroup);
        } else {
            em.merge(dictClientGroup);
        }
    }

    @Override
    public DictClientGroup getReference(Long id) {
        return em.getReference(DictClientGroup.class, id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<DictClientGroup> findAllRoot() {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<DictClientGroup> criteriaQuery = criteriaBuilder
                .createQuery(DictClientGroup.class);
        Root<DictClientGroup> root = criteriaQuery.from(DictClientGroup.class);
        criteriaQuery.where(criteriaBuilder.isNull(root
                .get(DictClientGroup_.dictClientGroup)));
        TypedQuery<DictClientGroup> typedQuery = em.createQuery(criteriaQuery);
        return typedQuery.getResultList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<DictClientGroup> findAllHierarchy() {
        throw new RuntimeException();
        // CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        // CriteriaQuery<DictClientGroup> criteriaQuery =
        // criteriaBuilder.createQuery(DictClientGroup.class);
        // Root<DictClientGroup> root =
        // criteriaQuery.from(DictClientGroup.class);
        // criteriaQuery.where(criteriaBuilder.isNull(root.get(DictClientGroup_.dictClientGroup)));
        // TypedQuery<DictClientGroup> typedQuery =
        // em.createQuery(criteriaQuery);
        // return typedQuery.getResultList();
    }
}
