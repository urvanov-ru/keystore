package ru.urvanov.keystore.dao;

import java.math.BigInteger;
import java.util.List;

import ru.urvanov.keystore.domain.DictAction;
import ru.urvanov.keystore.domain.Key;
import ru.urvanov.keystore.domain.KeyListItem;
import ru.urvanov.keystore.domain.KeyListParameters;

public interface KeyDao {
    Key findById(Long id);

    Key getReference(Long id);

    void save(Key key);

    List<KeyListItem> list(KeyListParameters param);

    BigInteger countList(KeyListParameters countParam);

    List<Key> findKeysByDictAction(DictAction action);

    List<Key> findByClientId(Long clientId);

    void delete(Long id);
}
