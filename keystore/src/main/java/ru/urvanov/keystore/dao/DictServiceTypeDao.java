package ru.urvanov.keystore.dao;

import java.util.Collection;
import java.util.List;

import ru.urvanov.keystore.domain.Client;
import ru.urvanov.keystore.domain.DictServiceType;

public interface DictServiceTypeDao {
    DictServiceType findById(Long id);

    DictServiceType getReference(Long id);

    List<DictServiceType> findAll();

    void save(DictServiceType dictServiceType);

    List<DictServiceType> findByName(String query, int start, int limit);

    Collection<DictServiceType> findByClient(Client client);
}
