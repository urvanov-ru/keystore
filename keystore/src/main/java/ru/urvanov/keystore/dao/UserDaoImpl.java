package ru.urvanov.keystore.dao;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.MapJoin;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.SetJoin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ru.urvanov.keystore.domain.Authority;
import ru.urvanov.keystore.domain.Authority_;
import ru.urvanov.keystore.domain.Client_;
import ru.urvanov.keystore.domain.DictEvent;
import ru.urvanov.keystore.domain.DictEventCode;
import ru.urvanov.keystore.domain.LinkUserDictEventNotification;
import ru.urvanov.keystore.domain.LinkUserDictEventNotification_;
import ru.urvanov.keystore.domain.User;
import ru.urvanov.keystore.domain.UserAccess;
import ru.urvanov.keystore.domain.UserAccessCode;
import ru.urvanov.keystore.domain.UserAccess_;
import ru.urvanov.keystore.domain.UserListItem;
import ru.urvanov.keystore.domain.UserListParameters;
import ru.urvanov.keystore.domain.User_;

@Repository("userDao")
public class UserDaoImpl implements UserDao {

    private static final Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);
    
    @PersistenceContext
    private EntityManager em;

    @Autowired
    private DictEventDao dictEventDao;

    @Transactional(readOnly = true)
    @Override
    public User findById(Long id) {
        return em.find(User.class, id);
    }

    @Transactional(readOnly = true)
    @Override
    public User findFullById(Long id) {
        logger.debug("id={}.", id);
        User user = em.find(User.class, id);
        if (user != null) {
            logger.debug("User.authorities.size={}.", user.getAuthorities().size());
            logger.debug("User.userAccess.size={}.", user.getUserAccesses().size());
            logger.debug("user.linkUserDictEventNotifications.size={}.", user.getLinkUserDictEventNotifications().size());
        }
        return user;
    }

    @Override
    public User getReference(Long id) {
        return em.getReference(User.class, id);
    }

    @Transactional(readOnly = true)
    @Override
    public User findByUserName(String userName) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder
                .createQuery(User.class);
        Root<User> rootUser = criteriaQuery.from(User.class);
        criteriaQuery.where(criteriaBuilder.equal(
                criteriaBuilder.lower(rootUser.get(User_.userName)),
                userName.toLowerCase()));
        criteriaQuery.select(rootUser);
        TypedQuery<User> typedQuery = em.createQuery(criteriaQuery);
        List<User> result = typedQuery.getResultList();
        if (result.size() > 0) {
            return result.get(0);
        } else {
            return null;
        }
    }

    @Transactional
    @Override
    public void save(User user) {
        if (user.getId() == null) {
            em.persist(user);
        } else {
            em.merge(user);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public User findFullByUserName(String username) {
        logger.debug("username={}.", username);
        User user = findByUserName(username);
        if (user != null) {
            logger.debug("User.authorities.size={}.", user.getAuthorities().size());
            logger.debug("User.userAccess.size={}.", user.getUserAccesses().size());
            logger.debug("user.linkUserDictEventNotifications.size={}.", user.getLinkUserDictEventNotifications().size());
        }
        return user;
    }

    @Transactional(readOnly = true)
    @Override
    public boolean checkUserNameUnique(User user) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder
                .createQuery(User.class);
        Root<User> rootUser = criteriaQuery.from(User.class);
        criteriaQuery.where(criteriaBuilder.notEqual(criteriaBuilder
                .literal(user.getId() == null ? 0 : user.getId()), rootUser
                .get(User_.id)), criteriaBuilder.equal(criteriaBuilder
                .lower(rootUser.get(User_.userName)), user.getUserName()
                .toLowerCase()));
        TypedQuery<User> typedQuery = em.createQuery(criteriaQuery);
        if (typedQuery.getResultList().size() > 0) {
            return false;
        } else {
            return true;
        }
    }

    @Transactional
    @Override
    public List<UserListItem> list(UserListParameters params) {
        StoredProcedureCall storedProcedureCall = new StoredProcedureCall();
        storedProcedureCall.setProcedureName("list_user");
        storedProcedureCall.addParameter("bigint", params.getUserId());
        return storedProcedureCall.getResultList(em, UserListItem.class);
    }

    @Transactional
    @Override
    public BigInteger countList(UserListParameters params) {
        StoredProcedureCall storedProcedureCall = new StoredProcedureCall();
        storedProcedureCall.setProcedureName("list_user");
        storedProcedureCall.addParameter("bigint", params.getUserId());
        return storedProcedureCall.getCount(em, UserListItem.class);
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> findEnabledByClientId(Long clientId) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder
                .createQuery(User.class);
        Root<User> rootUser = criteriaQuery.from(User.class);
        criteriaQuery.where(criteriaBuilder.equal(rootUser.get(User_.client)
                .get(Client_.id), clientId), criteriaBuilder.equal(
                rootUser.get(User_.enabled), true));
        criteriaQuery.orderBy(criteriaBuilder.asc(rootUser.get(User_.id)));
        TypedQuery<User> typedQuery = em.createQuery(criteriaQuery);
        return typedQuery.getResultList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> findForNewClientNotification() {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder
                .createQuery(User.class);
        Root<User> rootUser = criteriaQuery.from(User.class);
        SetJoin<User, Authority> joinAuthority = rootUser
                .join(User_.authorities);
        MapJoin<User, DictEvent, LinkUserDictEventNotification> joinLinkUserDictEventNotifications = rootUser
                .join(User_.linkUserDictEventNotifications);
        MapJoin<User, UserAccessCode, UserAccess> joinAccesses = rootUser
                .join(User_.userAccesses);
        criteriaQuery.where(criteriaBuilder.equal(
                joinAuthority.get(Authority_.authority), "ROLE_SERVICE"),
                criteriaBuilder.equal(joinAccesses.get(UserAccess_.code),
                        UserAccessCode.SERVICE_ADMIN_CLIENT), criteriaBuilder
                        .equal(joinAccesses.get(UserAccess_.access), true),
                criteriaBuilder.equal(joinLinkUserDictEventNotifications
                        .get(LinkUserDictEventNotification_.dictEvent),
                        dictEventDao.findByCode(DictEventCode.CLIENT_ADDED)),
                criteriaBuilder.equal(joinLinkUserDictEventNotifications
                        .get(LinkUserDictEventNotification_.allowNotification),
                        true));
        criteriaQuery.distinct(true);
        TypedQuery<User> typedQuery = em.createQuery(criteriaQuery);
        return typedQuery.getResultList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> findByTodayBirthday() {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder
                .createQuery(User.class);
        Root<User> rootUser = criteriaQuery.from(User.class);
        criteriaQuery.where(criteriaBuilder.equal(
                criteriaBuilder.currentDate(), rootUser.get(User_.birthdate)));
        TypedQuery<User> typedQuery = em.createQuery(criteriaQuery);
        return typedQuery.getResultList();
    }
}
