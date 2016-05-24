package ru.urvanov.keystore.dao;

import java.util.List;

import ru.urvanov.keystore.domain.DictClientGroup;

public interface DictClientGroupDao {
    DictClientGroup findById(Long id);

    List<DictClientGroup> findAll();

    void save(DictClientGroup dictClientGroup);

    DictClientGroup getReference(Long id);

    List<DictClientGroup> findAllRoot();

    List<DictClientGroup> findAllHierarchy();
}
