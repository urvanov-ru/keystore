package ru.urvanov.keystore.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ru.urvanov.keystore.domain.Client;
import ru.urvanov.keystore.domain.DictServiceType;
import ru.urvanov.keystore.domain.DictServiceTypeStatus;
import ru.urvanov.keystore.domain.DictServiceType_;

@Repository("dictServiceTypeDao")
public class DictServiceTypeDaoImpl implements DictServiceTypeDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public DictServiceType findById(Long id) {
        return em.find(DictServiceType.class, id);
    }

    @Override
    public DictServiceType getReference(Long id) {
        return em.getReference(DictServiceType.class, id);
    }

    @Override
    public List<DictServiceType> findAll() {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<DictServiceType> criteriaQuery = criteriaBuilder
                .createQuery(DictServiceType.class);
        criteriaQuery.from(DictServiceType.class);
        TypedQuery<DictServiceType> typedQuery = em.createQuery(criteriaQuery);
        return typedQuery.getResultList();
    }

    @Override
    public void save(DictServiceType dictServiceType) {
        if (dictServiceType.getId() == null) {
            em.persist(dictServiceType);
        } else {
            em.merge(dictServiceType);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<DictServiceType> findByName(String query, int start, int limit) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<DictServiceType> criteriaQuery = criteriaBuilder
                .createQuery(DictServiceType.class);
        Root<DictServiceType> rootDictServiceType = criteriaQuery
                .from(DictServiceType.class);
        criteriaQuery.where(criteriaBuilder.like(
                rootDictServiceType.get(DictServiceType_.name), "%" + query
                        + "%"));
        criteriaQuery.orderBy(criteriaBuilder.asc(rootDictServiceType
                .get(DictServiceType_.name)));
        TypedQuery<DictServiceType> typedQuery = em.createQuery(criteriaQuery);
        typedQuery.setFirstResult(start);
        typedQuery.setMaxResults(limit);
        return typedQuery.getResultList();
    }

    @Transactional
    @Override
    public Collection<DictServiceType> findByClient(Client fullClient) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<DictServiceType> criteriaQuery = criteriaBuilder
                .createQuery(DictServiceType.class);
        Root<DictServiceType> rootDictServiceType = criteriaQuery
                .from(DictServiceType.class);
        List<Long> linkDictServiceTypeIds = new ArrayList<Long>();
        for (DictServiceType dct : fullClient.getLinkClientDictServiceTypes()) {
            linkDictServiceTypeIds.add(dct.getId());
        }

        Predicate predicate = null;
        if (linkDictServiceTypeIds.size() == 0) {
            predicate = criteriaBuilder.equal(
                    rootDictServiceType.get(DictServiceType_.status),
                    DictServiceTypeStatus.ACTIVE);
        } else {
            predicate = criteriaBuilder.or(
                    criteriaBuilder.equal(
                            rootDictServiceType.get(DictServiceType_.status),
                            DictServiceTypeStatus.ACTIVE),
                    rootDictServiceType.get(DictServiceType_.id).in(
                            linkDictServiceTypeIds));
        }
        criteriaQuery.where(predicate);
        TypedQuery<DictServiceType> typedQuery = em.createQuery(criteriaQuery);
        return typedQuery.getResultList();
    }

}
