package ru.urvanov.keystore.service;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.urvanov.keystore.dao.DictClientGroupDao;
import ru.urvanov.keystore.domain.DictClientGroup;

@Service("dictClientGroupService")
public class DictClientGroupServiceImpl implements DictClientGroupService {

    @Autowired
    private DictClientGroupDao dictClientGroupDao;

    @Transactional(readOnly = true)
    @Override
    public DictClientGroup findById(Long id) {
        return dictClientGroupDao.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<DictClientGroup> findAll() {
        return dictClientGroupDao.findAll();
    }

    @Transactional
    @Override
    public void save(DictClientGroup dictClientGroup) {
        dictClientGroupDao.save(dictClientGroup);
    }

    @Override
    public DictClientGroup getReference(Long id) {
        return dictClientGroupDao.getReference(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<DictClientGroup> findAllRoot() {
        return dictClientGroupDao.findAllRoot();
    }

    @Transactional(readOnly = true)
    @Override
    public List<DictClientGroup> findAllHierarchy() {
        List<DictClientGroup> rootList= dictClientGroupDao.findAllRoot();
        Queue<DictClientGroup> queue = new ArrayDeque<DictClientGroup>(rootList);
        while (!queue.isEmpty() ) {
            DictClientGroup dcg = queue.remove();
            if (dcg.getChildDictClientGroups().size() > 0) {
                queue.addAll(dcg.getChildDictClientGroups());
            }
        }
        return rootList;
    }
}
