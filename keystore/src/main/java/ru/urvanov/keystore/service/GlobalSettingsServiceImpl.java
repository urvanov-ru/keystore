package ru.urvanov.keystore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.urvanov.keystore.dao.GlobalSettingsDao;
import ru.urvanov.keystore.domain.GlobalSettings;

@Service("globalSettingsService")
public class GlobalSettingsServiceImpl implements GlobalSettingsService {

    @Autowired
    private GlobalSettingsDao globalSettingsDao;

    @Transactional(readOnly = true)
    @Override
    public GlobalSettings read() {
        return globalSettingsDao.findById(1L);
    }

    @Transactional
    @Override
    public void save(GlobalSettings globalSettings) {
        globalSettingsDao.save(globalSettings);
    }

}
