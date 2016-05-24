package ru.urvanov.keystore.dao;

import java.util.List;

import ru.urvanov.keystore.domain.DictEvent;
import ru.urvanov.keystore.domain.DictEventCode;

public interface DictEventDao {
    DictEvent findById(Long id);

    DictEvent getReference(Long id);

    void save(DictEvent dictEvent);

    DictEvent findByCode(DictEventCode code);

    List<DictEvent> findAll();
}
