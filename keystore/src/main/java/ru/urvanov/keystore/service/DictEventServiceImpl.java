package ru.urvanov.keystore.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.urvanov.keystore.dao.DictEventDao;
import ru.urvanov.keystore.domain.DictEvent;
import ru.urvanov.keystore.domain.DictEventCode;

@Service("dictEventService")
public class DictEventServiceImpl implements DictEventService {

    @Autowired
    private DictEventDao dictEventDao;

    @Transactional(readOnly = true)
    @Override
    public DictEvent findById(Long id) {
        return dictEventDao.findById(id);
    }

    @Override
    public DictEvent getReference(Long id) {
        return dictEventDao.getReference(id);
    }

    @Transactional
    @Override
    public void save(DictEvent dictEvent) {
        dictEventDao.save(dictEvent);
    }

    @Transactional(readOnly = true)
    @Override
    public DictEvent findByCode(DictEventCode code) {
        return dictEventDao.findByCode(code);
    }

    @Transactional(readOnly = true)
    @Override
    public List<DictEvent> findAll() {
        return dictEventDao.findAll();
    }

}
