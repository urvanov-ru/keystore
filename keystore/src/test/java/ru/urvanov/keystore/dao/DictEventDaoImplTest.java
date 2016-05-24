package ru.urvanov.keystore.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.FileInputStream;
import java.sql.Connection;

import javax.sql.DataSource;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import ru.urvanov.keystore.dao.DictEventDao;
import ru.urvanov.keystore.domain.DictEvent;
import ru.urvanov.keystore.domain.DictEventCode;
import ru.urvanov.keystore.test.SpringTestBase;

public class DictEventDaoImplTest extends SpringTestBase {
    
    @Autowired
    private DictEventDao dictEventDao;
    
    @Autowired
    private DataSource dataSource;
    
    @Before
    public void before() throws Exception {

        // initialize your database connection here
        IDatabaseConnection databaseConnection = null;
        // ...

        // initialize your dataset here
        IDataSet dataSet = new FlatXmlDataSetBuilder().build(new FileInputStream("src/test/java/ru/urvanov/keystore/dao/DictEventDaoImplTest.xml"));

        try (Connection connection = dataSource.getConnection())
        {
            databaseConnection = new DatabaseConnection(connection);
            DatabaseOperation.CLEAN_INSERT.execute(databaseConnection, dataSet);
        }
    }
    
    @Test
    @Transactional
    public void testFindById1() {
        DictEvent dictEvent = dictEventDao.findById(1L);
        assertNotNull(dictEvent);
        assertEquals(DictEventCode.CLIENT_ADDED, dictEvent.getCode());
    }
    
    @Test
    @Transactional
    public void testFindById2() {
        assertNull(dictEventDao.findById(-1L));
    }
    
    @Test
    @Transactional
    public void testSave() {
        DictEvent dictEvent = new DictEvent();
        dictEvent.setId(100L);
        dictEvent.setCode(DictEventCode.CLIENT_BLOCKED);
        dictEventDao.save(dictEvent);
        Long id = dictEvent.getId();
        assertNotNull(id);
        dictEvent = dictEventDao.findById(id);
        assertNotNull(dictEvent);
        assertEquals(id, dictEvent.getId());
        assertEquals(DictEventCode.CLIENT_BLOCKED, dictEvent.getCode());
    }
    
    @Test
    @Transactional
    public void testFindAll() {
        assertEquals(16, dictEventDao.findAll().size());
    }
}
