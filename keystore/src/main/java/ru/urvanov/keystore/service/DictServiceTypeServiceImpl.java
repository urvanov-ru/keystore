package ru.urvanov.keystore.service;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.urvanov.keystore.dao.DictServiceTypeDao;
import ru.urvanov.keystore.domain.Client;
import ru.urvanov.keystore.domain.DictServiceType;

@Service("dictServiceTypeService")
public class DictServiceTypeServiceImpl implements DictServiceTypeService {

    @Autowired
    private DictServiceTypeDao dictServiceTypeDao;

    @Transactional(readOnly = true)
    @Override
    public DictServiceType findById(Long id) {
        return dictServiceTypeDao.findById(id);
    }

    @Override
    public DictServiceType getReference(Long id) {
        return dictServiceTypeDao.getReference(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<DictServiceType> findAll() {
        return dictServiceTypeDao.findAll();
    }

    @Transactional
    @Override
    public void save(DictServiceType dictServiceType) {
        dictServiceTypeDao.save(dictServiceType);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DictServiceType> findByName(String query, int start, int limit) {
        return dictServiceTypeDao.findByName(query, start, limit);
    }

    @Transactional(readOnly = true)
    @Override
    public Collection<DictServiceType> findByClient(Client fullClient) {
        return dictServiceTypeDao.findByClient(fullClient);
    }

}
