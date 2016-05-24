package ru.urvanov.keystore.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.urvanov.keystore.dao.ClientDao;
import ru.urvanov.keystore.dao.DictActionDao;
import ru.urvanov.keystore.dao.KeyDao;
import ru.urvanov.keystore.domain.Client;
import ru.urvanov.keystore.domain.DictAction;
import ru.urvanov.keystore.domain.DictServiceType;
import ru.urvanov.keystore.domain.Key;
import ru.urvanov.keystore.domain.KeyData;
import ru.urvanov.keystore.domain.KeyStatus;

@Service("dictActionService")
public class DictActionServiceImpl implements DictActionService {

    @Autowired
    private DictActionDao dictActionDao;
    
    @Autowired
    private ClientDao clientDao;
    
    @Autowired
    private MailService mailService;
    
    @Autowired
    private KeyDao keyDao;

    @Transactional(readOnly = true)
    @Override
    public DictAction findById(Long id) {
        return dictActionDao.findById(id);
    }

    @Override
    public DictAction getReference(Long id) {
        return dictActionDao.getReference(id);
    }

    @Transactional
    @Override
    public void save(DictAction dictAction) {
        dictActionDao.save(dictAction);
    }

    @Transactional(readOnly = true)
    @Override
    public List<DictAction> findAll() {
        return dictActionDao.findAll();
    }

    @Transactional
    @Override
    public void start(Long id, Date dateEnd) throws IOException {
        
        DictAction dictAction = dictActionDao.findById(id);
        
        DictServiceType dictServiceType = dictAction.getDictServiceType();
        String baseKey = dictServiceType.getBaseKey();
        KeyData baseKeyDataEntity = new KeyData();// parse from base key
        
        dictAction.setDateBegin(new Date());
        dictAction.setDateEnd(dateEnd);
        dictActionDao.save(dictAction);
        List<Client> clients = clientDao.findByDictActionId(dictAction.getId());
        for (Client client: clients) {
            
            
            long expired = System.currentTimeMillis(); // from entity
            long started = System.currentTimeMillis() - 30 * 24 * 60 * 60 * 1000; // from entity
            long difference = expired - started;
            long differenceDays = difference / 1000 / 60 / 60 / 24;
            long difference30Days = differenceDays / 30;
            for (long n = 0; n < difference30Days; n++) {
                Key key = new Key();
                key.setDictAction(dictAction);
                key.setStatus(KeyStatus.CREATED);
                key.setClient(client);
                keyDao.save(key);
            }
            mailService.sendNotificationGivenDictAction(client, dictAction);
        }
    }

}
