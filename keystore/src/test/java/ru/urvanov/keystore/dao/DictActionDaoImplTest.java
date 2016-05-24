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
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import ru.urvanov.keystore.dao.DictActionDao;
import ru.urvanov.keystore.dao.DictServiceTypeDao;
import ru.urvanov.keystore.domain.DictAction;
import ru.urvanov.keystore.domain.DictActionType;
import ru.urvanov.keystore.test.SpringTestBase;

public class DictActionDaoImplTest extends SpringTestBase {
    
    @Autowired
    private DictActionDao dictActionDao;
    
    @Autowired
    private DictServiceTypeDao dictServiceTypeDao;
    
    @Autowired
    private DataSource dataSource;
    
    @Before
    public void before() throws Exception {

        // initialize your database connection here
        IDatabaseConnection databaseConnection = null;
        // ...

        // initialize your dataset here
        IDataSet dataSet = new FlatXmlDataSetBuilder().build(new FileInputStream("src/test/java/ru/urvanov/keystore/dao/DictActionDaoImplTest.xml"));

        try (Connection connection = dataSource.getConnection())
        {
            databaseConnection = new DatabaseConnection(connection);
            DatabaseOperation.CLEAN_INSERT.execute(databaseConnection, dataSet);
        }
    }
    
    @Transactional
    @Test
    public void testFindById1() {
        DictAction dictAction = dictActionDao.findById(1L);
        assertNotNull(dictAction);
        assertEquals("first dict action", dictAction.getName());
        assertEquals("Описание первой акции", dictAction.getDescription());
    }
    
    @Transactional
    @Test
    public void testFindByid2() {
        assertNull(dictActionDao.findById(-1L));
    }
    
    @Transactional
    @Test
    public void save() {
        DictAction dictAction = new DictAction();
        dictAction.setName("супер акция");
        dictAction.setDescription("только сейчас");
        Date dateBegin = new Date();
        Date dateEnd = new Date();
        dictAction.setDateBegin(dateBegin);
        dictAction.setDateEnd(dateEnd);
        dictAction.setDictServiceType(dictServiceTypeDao.getReference(1L));
        dictAction.setForNewClients(true);
        dictAction.setDictActionType(DictActionType.GROUP);
        dictActionDao.save(dictAction);
        Long id = dictAction.getId();
        assertNotNull(id);
        dictActionDao.findById(id);
        assertEquals(id, dictAction.getId());
        assertEquals("супер акция", dictAction.getName());
        assertEquals("только сейчас", dictAction.getDescription());
        assertEquals(dateBegin, dictAction.getDateBegin());
        assertEquals(dateEnd, dictAction.getDateEnd());
    }
    
    @Transactional
    @Test
    public void findAll1(){
        assertEquals(2, dictActionDao.findAll().size());
    }

}
