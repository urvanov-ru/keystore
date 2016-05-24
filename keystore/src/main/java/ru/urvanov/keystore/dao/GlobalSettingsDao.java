package ru.urvanov.keystore.dao;

import ru.urvanov.keystore.domain.GlobalSettings;

public interface GlobalSettingsDao {
    GlobalSettings findById(Long id);

    void save(GlobalSettings globalSettings);
}
