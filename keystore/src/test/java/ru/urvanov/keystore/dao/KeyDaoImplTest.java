package ru.urvanov.keystore.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.FileInputStream;
import java.sql.Connection;
import java.util.Date;

import javax.sql.DataSource;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import ru.urvanov.keystore.domain.DictAction;
import ru.urvanov.keystore.domain.Key;
import ru.urvanov.keystore.domain.KeyListParameters;
import ru.urvanov.keystore.domain.KeyStatus;
import ru.urvanov.keystore.test.SpringTestBase;

public class KeyDaoImplTest extends SpringTestBase {
    @Autowired
    private KeyDao keyDao;
    
    @Autowired
    private OrderDao orderDao;
    
    @Autowired
    private DictActionDao dictActionDao;
    
    @Autowired
    private DataSource dataSource;
    
    @Autowired
    private ClientDao clientDao;
    
    @Before
    public void before() throws Exception {

        // initialize your database connection here
        IDatabaseConnection databaseConnection = null;
        // ...

        // initialize your dataset here
        FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
        builder.setColumnSensing(true);
        IDataSet dataSet = builder.build(new FileInputStream("src/test/java/ru/urvanov/keystore/dao/KeyDaoImplTest.xml"));

        try (Connection connection = dataSource.getConnection())
        {
            databaseConnection = new DatabaseConnection(connection);
            DatabaseOperation.CLEAN_INSERT.execute(databaseConnection, dataSet);
        }
    }
    
    @Transactional
    @Test
    public void testFindById1() {
        Key key = keyDao.findById(1L);
        assertNotNull(key);
        assertEquals(new Long(1L), key.getId());
        assertEquals(KeyStatus.CREATED, key.getStatus());
    }
    
    @Transactional
    @Test
    public void testFindById2() {
        assertNull(keyDao.findById(-1L));
    }
    
    @Transactional
    @Test
    public void testGetReference() {
        Key key = keyDao.getReference(1L);
        assertNotNull(key);
        assertEquals(new Long(1L), key.getId());
    }
    
    @Transactional
    @Test
    public void testSave() {
        Key key = new Key();
        Date dateBegin = new Date();
        key.setDateBegin(dateBegin);
        Date dateEnd = new Date();
        key.setDateEnd(dateEnd);
        key.setDictAction(dictActionDao.getReference(1L));
        key.setOrder(orderDao.getReference(1L));
        key.setStatus(KeyStatus.CREATED);
        key.setClient(clientDao.findById(1L));
        keyDao.save(key);
        Long id = key.getId();
        assertNotNull(id);
        key = keyDao.findById(id);
        assertNotNull(key);
        assertEquals(id, key.getId());
        assertEquals(dateBegin, key.getDateBegin());
        assertEquals(dateEnd, key.getDateEnd());
        assertNotNull(key.getDictAction());
        assertNotNull(key.getOrder());
        assertEquals(new Long(1L), key.getDictAction().getId());
        assertEquals(Long.valueOf(1L), key.getOrder().getId());
        assertEquals(KeyStatus.CREATED, key.getStatus());
    }
    
    @Test
    @Transactional
    public void testList1() {
        KeyListParameters param = new KeyListParameters();
        param.setUserId(0L);
        assertEquals(0, keyDao.list(param).size());
    }
    
    @Test
    @Transactional
    public void testCount1() {
        KeyListParameters param = new KeyListParameters();
        param.setUserId(0L);
        assertEquals(0, keyDao.countList(param).longValueExact());
    }
    
    @Test
    @Transactional
    public void testFindByAction() {
        DictAction action = new DictAction();
        action.setId(2L);
        assertEquals(2, this.keyDao.findKeysByDictAction(action).size());
    }
    
  
    @Test
    @Transactional
    public void testFindByClientId1() {
        assertEquals(3, this.keyDao.findByClientId(1L).size());
    }
}
