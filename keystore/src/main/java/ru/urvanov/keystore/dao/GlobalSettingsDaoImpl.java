package ru.urvanov.keystore.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ru.urvanov.keystore.domain.GlobalSettings;

@Repository("globalSettingsDao")
public class GlobalSettingsDaoImpl implements GlobalSettingsDao {

    @PersistenceContext
    private EntityManager em;

    @Transactional(readOnly = true)
    @Override
    public GlobalSettings findById(Long id) {
        return em.find(GlobalSettings.class, id);
    }

    @Transactional
    @Override
    public void save(GlobalSettings globalSettings) {
        em.merge(globalSettings);
    }

}
