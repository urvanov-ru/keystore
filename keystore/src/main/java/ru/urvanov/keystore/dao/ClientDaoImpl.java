/**
 * 
 */
package ru.urvanov.keystore.dao;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.SetJoin;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ru.urvanov.keystore.domain.Client;
import ru.urvanov.keystore.domain.ClientListItem;
import ru.urvanov.keystore.domain.ClientListParameters;
import ru.urvanov.keystore.domain.Client_;
import ru.urvanov.keystore.domain.DictAction;
import ru.urvanov.keystore.domain.DictAction_;

/**
 * @author fedya
 *
 */
@Repository("clientDao")
public class ClientDaoImpl implements ClientDao {

    @PersistenceContext
    private EntityManager em;

    /*
     * (non-Javadoc)
     * 
     * @see ru.urvanov.keystore.dao.ClientDao#findById(java.lang.Long)
     */
    @Override
    @Transactional(readOnly = true)
    public Client findById(Long id) {
        return em.find(Client.class, id);
    }

    @Override
    @Transactional(readOnly = true)
    public Client findFullById(Long id) {
        TypedQuery<Client> typedQuery = em.createNamedQuery(
                "findFullClientById", Client.class);
        typedQuery.setParameter("id", id);
        List<Client> result = typedQuery.getResultList();
        if (result.size() >= 1)
            return result.get(0);
        else
            return null;
    }

    @Override
    @Transactional(readOnly = true)
    public Client getReference(Long id) {
        return em.getReference(Client.class, id);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ru.urvanov.keystore.dao.ClientDao#save(ru.urvanov.keystore.domain.Client
     * )
     */
    @Override
    @Transactional
    public void save(Client client) {
        if (client.getId() == null) {
            em.persist(client);
        } else {
            em.merge(client);
        }
    }

    @Transactional
    @Override
    public List<ClientListItem> list(ClientListParameters clientListParameters) {
        StoredProcedureCall spc = new StoredProcedureCall();
        spc.setProcedureName("list_client");
        spc.addParameter("bigint", clientListParameters.getUserId());
        spc.addParameter("boolean", clientListParameters.getActive());
        spc.addParameter("bigint", clientListParameters.getDictClientGroupId());
        spc.addParameter("date", clientListParameters.getActiveBegin());
        spc.addParameter("date", clientListParameters.getActiveEnd());
        spc.addParameter("bigint", clientListParameters.getDictActionId());
        return spc.getResultList(em, ClientListItem.class);
    }

    @Override
    public BigInteger countList(ClientListParameters clientListParameters) {
        StoredProcedureCall spc = new StoredProcedureCall();
        spc.setProcedureName("list_client");
        spc.addParameter("bigint", clientListParameters.getUserId());
        spc.addParameter("boolean", clientListParameters.getActive());
        spc.addParameter("bigint", clientListParameters.getDictClientGroupId());
        spc.addParameter("date", clientListParameters.getActiveBegin());
        spc.addParameter("date", clientListParameters.getActiveEnd());
        spc.addParameter("bigint", clientListParameters.getDictActionId());
        return spc.getCount(em, ClientListItem.class);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean checkNameUnique(Client client) {
        String sql = "select * from client c " + "where upper(regexp_replace("
                + "c.name, '[ ?\\*\\()!@#$%^&*:;\"<.>.]', '','g')) "
                + "= upper(regexp_replace(:name, "
                + "'[ ?\\*\\()!@#$%^&*:;\"<.>.]', '','g')) ";
        if (client.getId() != null)
            sql += " and c.id <> cast(:id as bigint)";
        javax.persistence.Query query = em.createNativeQuery(sql);
        query.setParameter("name", client.getName());
        if (client.getId() != null)
            query.setParameter("id", client.getId());

        if (query.getResultList().size() > 0) {
            return false;
        } else {
            return true;
        }
    }

    @Transactional(readOnly=true)
    @Override
    public Client findByUniqueId(String uniqueId) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Client> criteriaQuery = criteriaBuilder
                .createQuery(Client.class);
        Root<Client> rootClient = criteriaQuery.from(Client.class);
        criteriaQuery.where(criteriaBuilder.equal(
                rootClient.get(Client_.uniqueId), uniqueId));
        TypedQuery<Client> typedQuery = em.createQuery(criteriaQuery);
        List<Client> result = typedQuery.getResultList();
        if (result.size() > 0)
            return result.get(0);
        else
            return null;
    }

    @Transactional(readOnly=true)
    @Override
    public Client findByAuthority(String authority) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Client> criteriaQuery = criteriaBuilder.createQuery(Client.class);
        Root<Client> rootClient = criteriaQuery.from(Client.class);
        criteriaQuery.where(criteriaBuilder.equal(rootClient.get(Client_.authority), authority));
        TypedQuery<Client> typedQuery = em.createQuery(criteriaQuery);
        List<Client> result = typedQuery.getResultList();
        if (result.size() > 0)
            return result.get(0);
        else 
            return null;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Client> findByDictActionId(Long dictActionId) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Client> criteriaQuery = criteriaBuilder.createQuery(Client.class);
        Root<Client> rootClient = criteriaQuery.from(Client.class);
        SetJoin<Client, DictAction> linkClientDictActions = rootClient.join(Client_.linkClientDictActions, JoinType.INNER);
        Predicate predicate =criteriaBuilder.equal(linkClientDictActions.get(DictAction_.id), dictActionId);
        criteriaQuery.where(predicate);
        criteriaQuery.distinct(true);
        TypedQuery<Client> typedQuery = em.createQuery(criteriaQuery);
        return typedQuery.getResultList();
    }

}
