package ru.urvanov.keystore.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.urvanov.keystore.dao.ClientDao;
import ru.urvanov.keystore.dao.DictActionDao;
import ru.urvanov.keystore.dao.DictServiceTypeDao;
import ru.urvanov.keystore.dao.UserDao;
import ru.urvanov.keystore.domain.Authority;
import ru.urvanov.keystore.domain.Client;
import ru.urvanov.keystore.domain.ClientListItem;
import ru.urvanov.keystore.domain.ClientListParameters;
import ru.urvanov.keystore.domain.DictAction;
import ru.urvanov.keystore.domain.DictServiceType;
import ru.urvanov.keystore.domain.User;
import ru.urvanov.keystore.domain.UserAccess;
import ru.urvanov.keystore.domain.UserAccessCode;
import ru.urvanov.keystore.exception.ClientNameNotUniqueException;
import ru.urvanov.keystore.exception.UserNameNotUniqueException;

@Service("clientService")
public class ClientServiceImpl implements ClientService {

    private static final Logger logger = LoggerFactory
            .getLogger(ClientServiceImpl.class);

    @Autowired
    private ClientDao clientDao;

    @Autowired
    private BCryptPasswordEncoder bcryptEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private MailService mailService;

    @Autowired
    private DictServiceTypeDao dictServiceTypeDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private DictActionDao dictActionDao;

    @Transactional(readOnly = true)
    @Override
    public Client findById(Long id) {
        return clientDao.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public Client findFullById(Long id) {
        return clientDao.findFullById(id);
    }

    @Override
    public Client getReference(Long id) {
        return clientDao.getReference(id);
    }

    @Transactional
    @Override
    public void save(Client client) throws ClientNameNotUniqueException,
            UserNameNotUniqueException {
        save(client, null);
    }

    @Transactional
    @Override
    public List<ClientListItem> list(ClientListParameters clientListParameters) {
        return clientDao.list(clientListParameters);
    }

    @Transactional
    @Override
    public BigInteger countList(ClientListParameters clientListParameters) {
        return clientDao.countList(clientListParameters);
    }

    @Transactional
    @Override
    public Client findByUniqueId(String uniqueId) {
        return clientDao.findByUniqueId(uniqueId);
    }

    @Transactional(rollbackFor=Exception.class)
    @Override
    public void save(Client client, String userPassword)
            throws ClientNameNotUniqueException, UserNameNotUniqueException {
        boolean newClient = client.getId() == null;
        if (!clientDao.checkNameUnique(client))
            throw new ClientNameNotUniqueException();
        if (newClient) {
            client.setUniqueId(RandomStringUtils.randomAlphanumeric(10));
            client.setPassword(bcryptEncoder.encode(RandomStringUtils
                    .randomAlphanumeric(10)));
        }
        clientDao.save(client);

        if (newClient) {
            client.setUniqueId(RandomStringUtils.randomAlphanumeric(10) + Long.toHexString(client.getId()));
            clientDao.save(client);
            
            User user = new User();
            user.setFullName(client.getContactPersonName());
            user.setEmail(client.getContactPersonEmail());
            user.setEnabled(true);
            user.setActivated(false);
            user.setClient(client);
            Set<Authority> authorities = new HashSet<Authority>();
            Authority authorityClientAdmin = new Authority();
            authorityClientAdmin.setAuthority("ROLE_CLIENT");
            authorityClientAdmin.setUser(user);
            authorities.add(authorityClientAdmin);
            user.setAuthorities(authorities);
            for (UserAccessCode userAccessCode : UserAccessCode.values()) {
                if (userAccessCode.name().startsWith("CLIENT_")) {
                    UserAccess userAccess = new UserAccess();
                    userAccess.setAccess(true);
                    userAccess.setUser(user);
                    userAccess.setCode(userAccessCode);
                    user.getUserAccesses().put(userAccessCode, userAccess);
                }
            }
            user.setPassword(bcryptEncoder
                    .encode(userPassword == null ? RandomStringUtils
                            .randomAlphanumeric(10) : userPassword));
            userService.save(user);
            mailService.sendNotificationNewClient(client, user);
        } else {
            mailService.sendNotificationChangeClient(client);
        }
    }

    @Transactional
    @Override
    public void blockById(Long id) {
        Client client = findById(id);
        client.setActive(false);
        try {
            save(client);
            mailService.sendNotificationBlockClient(client);
        } catch (ClientNameNotUniqueException | UserNameNotUniqueException e) {
            logger.error("blockById failed.", e);
        }
    }

    @Transactional
    @Override
    public void giveDictServiceType(Client client, Long... dictServiceTypeIds) {
        Client fullClient = clientDao.findFullById(client.getId());
        boolean added = false;
        Map<Long, DictServiceType> dstMap = new HashMap<Long, DictServiceType>();
        for (Long dictServiceTypeId : dictServiceTypeIds) {
            DictServiceType dictServiceType = dictServiceTypeDao
                    .findById(dictServiceTypeId);
            if (!fullClient.getLinkClientDictServiceTypes().contains(
                    dictServiceType)) {
                fullClient.getLinkClientDictServiceTypes().add(dictServiceType);
                added = true;
                dstMap.put(dictServiceTypeId, dictServiceType);
            }
        }
        if (added) {
            clientDao.save(fullClient);
            for (DictServiceType dictServiceType : dstMap.values()) {
                mailService.sendNotificationGivenDictServiceType(client,
                        dictServiceType);
            }
        }
    }

    @Transactional
    @Override
    public void removeDictServiceType(Client client, Long... dictServiceTypeIds) {
        Client fullClient = clientDao.findFullById(client.getId());
        List<DictServiceType> removeList = new ArrayList<>();
        for (DictServiceType dct : fullClient.getLinkClientDictServiceTypes()) {
            for (Long dictServiceTypeId : dictServiceTypeIds) {
                if (dct.getId().equals(dictServiceTypeId))
                    removeList.add(dct);
            }
        }
        fullClient.getLinkClientDictServiceTypes().removeAll(removeList);
        clientDao.save(fullClient);
    }

    @Transactional
    @Override
    public Client findService() {
        return clientDao.findByAuthority("ROLE_SERVICE");
    }

    @Transactional
    @Override
    public void giveDictAction(Client client, Long... dictActionIds) {
        Client fullClient = clientDao.findFullById(client.getId());
        boolean added = false;
        Map<Long, DictAction> dstMap = new HashMap<Long, DictAction>();
        for (Long dictActionId : dictActionIds) {
            DictAction dictAction = dictActionDao.findById(dictActionId);
            if (!fullClient.getLinkClientDictActions().contains(dictAction)) {
                fullClient.getLinkClientDictActions().add(dictAction);
                added = true;
                dstMap.put(dictActionId, dictAction);
            }
        }
        if (added) {
            clientDao.save(fullClient);
        }
    }

    @Transactional
    @Override
    public void removeDictAction(Client client, Long... dictActionIds) {
        Client fullClient = clientDao.findFullById(client.getId());
        List<DictAction> removeList = new ArrayList<>();
        for (DictAction da : fullClient.getLinkClientDictActions()) {
            for (Long dictActionId : dictActionIds) {
                if (da.getId().equals(dictActionId))
                    removeList.add(da);
            }
        }
        fullClient.getLinkClientDictActions().removeAll(removeList);
        clientDao.save(fullClient);
    }

    @Transactional
    @Override
    public void changePassword(Client client, String newPassword) {
        client = clientDao.findById(client.getId());
        client.setPassword(bcryptEncoder.encode(newPassword));
        clientDao.save(client);
    }
}
