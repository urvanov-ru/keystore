package ru.urvanov.keystore.dao;

import java.util.List;

import ru.urvanov.keystore.domain.DictAction;

public interface DictActionDao {
    DictAction findById(Long id);

    DictAction getReference(Long id);

    void save(DictAction dictAction);

    List<DictAction> findAll();
}
