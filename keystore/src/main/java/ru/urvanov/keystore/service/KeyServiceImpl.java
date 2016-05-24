package ru.urvanov.keystore.service;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.urvanov.keystore.dao.KeyDao;
import ru.urvanov.keystore.dao.OrderDao;
import ru.urvanov.keystore.domain.DictAction;
import ru.urvanov.keystore.domain.DictServiceType;
import ru.urvanov.keystore.domain.Key;
import ru.urvanov.keystore.domain.KeyData;
import ru.urvanov.keystore.domain.KeyListItem;
import ru.urvanov.keystore.domain.KeyListParameters;
import ru.urvanov.keystore.domain.KeyStatus;
import ru.urvanov.keystore.domain.Order;
import ru.urvanov.keystore.domain.OrderStatus;

@Service("keyService")
public class KeyServiceImpl implements KeyService {

    @Autowired
    private KeyDao keyDao;

    @Autowired
    private OrderDao orderDao;

    @Transactional(readOnly = true)
    @Override
    public Key findById(Long id) {
        return keyDao.findById(id);
    }

    @Transactional
    @Override
    public void save(Key key) {
        keyDao.save(key);
    }

    @Transactional
    @Override
    public List<KeyListItem> list(KeyListParameters param) {
        return keyDao.list(param);
    }

    @Transactional
    @Override
    public BigInteger countList(KeyListParameters countParam) {
        return keyDao.countList(countParam);
    }

    @Transactional
    @Override
    public void activateAndSaveKey(Key key) throws IOException {
        if (key.getStatus() != KeyStatus.CREATED)
            throw new IllegalArgumentException("key");

        Order order = key.getOrder();
        DictAction action = key.getDictAction();
        Date activationDate = new Date();

        LastActivatedKeyInfo lastActivatedKeyInfo = null;
        DictServiceType dictServiceType = null;
        if (order != null) {
            lastActivatedKeyInfo = this.findLastActivatedKeyInfo(order);
            dictServiceType = order.getDictServiceType();
        }
        if (action != null) {
            lastActivatedKeyInfo = this.findLastActivatedKeyInfo(action);
            dictServiceType = action.getDictServiceType();
        }

        String baseKey = dictServiceType.getBaseKey();
        KeyData dictServiceTypeKeyDataEntity = new KeyData();// from baseKey
        long dictServiceTypeStarted = 0;// from dictServiceTypeKeyDataEntity
        long dictServiceTypeExpired = System.currentTimeMillis() + 1_000_000_000L; // from
                                                      // dictServiceTypeKeyDataEntity
        long dictServiceTypeDifference = dictServiceTypeExpired
                - dictServiceTypeStarted;
        long dictServiceTypeDifferenceDays = dictServiceTypeDifference / 1000
                / 60 / 60 / 24;

        long remainingDays = dictServiceTypeDifferenceDays;
        if (lastActivatedKeyInfo != null) {
            remainingDays -= lastActivatedKeyInfo.getUsedPeriod();

            if (remainingDays <= 0)
                throw new IllegalStateException(
                        "remainingDays is less or equal then zero ("
                                + remainingDays + ") . It's error.");

            if (lastActivatedKeyInfo.getKey().getDateEnd()
                    .after(activationDate))
                activationDate = lastActivatedKeyInfo.getKey().getDateEnd();
        }
        long activationDays = remainingDays >= 30 ? 30 : remainingDays;
        Calendar activationCalendar = Calendar.getInstance();
        activationCalendar.setTime(activationDate);
        activationCalendar.add(Calendar.DATE, (int) activationDays);
        Date expiredDate = activationCalendar.getTime();

        String stringBaseKey = dictServiceType.getBaseKey();
        KeyData keyDataEntityBase = new KeyData();// from stringBaseKey

        KeyData keyEntityBase = createFromBase(keyDataEntityBase,
                String.valueOf(key.getClient().getId()), "clientPassword",
                activationDate.getTime(), expiredDate.getTime());

        String stringKey = "";// from keyEntityBase
        key.setCode(stringKey);
        key.setStatus(KeyStatus.ACTIVE);
        key.setDateBegin(activationDate);
        key.setDateEnd(expiredDate);
        if (order != null) {
            save(key);
            boolean orderCompleted = true;
            order = orderDao.findById(order.getId());
            for (Key childKey : order.getKeys()) {
                if (childKey.getStatus() != KeyStatus.ACTIVE)
                    orderCompleted = false;
            }
            if (orderCompleted) {
                order.setStatus(OrderStatus.COMPLETED);
                order.setCompletedDateTime(new Date());
            }
            orderDao.save(order);
        } else {
            save(key);
        }

    }

    private KeyData createFromBase(KeyData keyEntityBase, String clientId,
            String password, long started, long expired) {
        KeyData keyEntity = new KeyData();

        return keyEntity;
    }

    private class LastActivatedKeyInfo {
        private Key key;
        private long usedPeriod;

        public LastActivatedKeyInfo(Key key, long usedPeriod) {
            this.key = key;
            this.usedPeriod = usedPeriod;
        }

        public Key getKey() {
            return key;
        }

        public long getUsedPeriod() {
            return usedPeriod;
        }
    }

    private LastActivatedKeyInfo findLastActivatedKeyInfo(Order order) {
        Order foundOrder = orderDao.findFullById(order.getId());
        Set<Key> keys = foundOrder.getKeys();
        return getLastActivatedKeyInfo(keys);
    }

    private LastActivatedKeyInfo findLastActivatedKeyInfo(DictAction action) {
        List<Key> keys = this.findKeysByDictAction(action);
        return getLastActivatedKeyInfo(keys);
    }

    private LastActivatedKeyInfo getLastActivatedKeyInfo(Collection<Key> keys) {
        long usedPeriod = 0L;
        Date lastDateEnd = null;
        Key lastActivatedKey = null;
        for (Key key : keys) {
            if (key.getDateBegin() != null) {
                long millisBegin = key.getDateBegin().getTime();
                long millisEnd = key.getDateEnd().getTime();
                long difference = millisEnd - millisBegin;
                long differenceDays = difference / 1000 / 60 / 60 / 24;
                usedPeriod += differenceDays;
                if (lastDateEnd == null || key.getDateEnd().after(lastDateEnd)) {
                    lastDateEnd = key.getDateEnd();
                    lastActivatedKey = key;
                }
            }
        }
        if (lastActivatedKey != null) {
            return new LastActivatedKeyInfo(lastActivatedKey, usedPeriod);
        } else {
            return null;
        }
    }

    private List<Key> findKeysByDictAction(DictAction action) {
        return keyDao.findKeysByDictAction(action);
    }

    @Override
    public String generateKey(KeyData keyData) {
        return "keyData to string";
    }

    @Transactional
    @Override
    public List<Key> findByClientId(Long clientId) {
        return keyDao.findByClientId(clientId);
    }

    @Transactional
    @Override
    public void delete(Key key) {
        keyDao.delete(key.getId());
    }
}
