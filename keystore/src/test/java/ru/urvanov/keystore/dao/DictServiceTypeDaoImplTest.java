package ru.urvanov.keystore.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.FileInputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;

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

import ru.urvanov.keystore.dao.ClientDao;
import ru.urvanov.keystore.dao.DictServiceTypeDao;
import ru.urvanov.keystore.domain.Client;
import ru.urvanov.keystore.domain.DictServiceType;
import ru.urvanov.keystore.domain.DictServiceTypeStatus;
import ru.urvanov.keystore.test.SpringTestBase;

public class DictServiceTypeDaoImplTest extends SpringTestBase {

    @Autowired
    private DictServiceTypeDao dictServiceTypeDao;

    @Autowired
    private ClientDao clientDao;

    @Autowired
    private DataSource dataSource;
    
    @Before
    public void before() throws Exception {

        // initialize your database connection here
        IDatabaseConnection databaseConnection = null;
        // ...

        // initialize your dataset here
        IDataSet dataSet = new FlatXmlDataSetBuilder().build(new FileInputStream("src/test/java/ru/urvanov/keystore/dao/DictServiceTypeDaoImplTest.xml"));

        try (Connection connection = dataSource.getConnection())
        {
            databaseConnection = new DatabaseConnection(connection);
            DatabaseOperation.CLEAN_INSERT.execute(databaseConnection, dataSet);
        }
    }
    
    @Test
    @Transactional
    public void testFindById1() {
        DictServiceType dictServiceType = dictServiceTypeDao.findById(1L);
        assertNotNull(dictServiceType);
        assertEquals("first dict service type name", dictServiceType.getName());
    }

    @Test
    @Transactional
    public void testFindById2() {
        DictServiceType dictServiceType = dictServiceTypeDao.findById(-1L);
        assertNull(dictServiceType);
    }

    @Test
    @Transactional
    public void testFindAll() {
        List<DictServiceType> result = dictServiceTypeDao.findAll();
        assertNotNull(result);
        assertEquals(4, result.size());
    }

    @Test
    @Transactional
    public void testSave() {
        DictServiceType dictServiceType = new DictServiceType();
        dictServiceType.setName("new dict service type");
        dictServiceType.setDescription("Описание сложное");
        dictServiceType.setAmount(new BigDecimal("34.56"));
        dictServiceType.setStatus(DictServiceTypeStatus.ACTIVE);
        dictServiceType.setAmount30Days(new BigDecimal("20.00"));
        dictServiceTypeDao.save(dictServiceType);
        Long id = dictServiceType.getId();
        assertNotNull(id);
        dictServiceType = dictServiceTypeDao.findById(id);
        assertNotNull(dictServiceType);
        assertEquals("new dict service type", dictServiceType.getName());
        assertEquals("Описание сложное", dictServiceType.getDescription());
        assertEquals(0,
                new BigDecimal("34.56").compareTo(dictServiceType.getAmount()));
        assertEquals(DictServiceTypeStatus.ACTIVE, dictServiceType.getStatus());
    }

    @Test
    @Transactional
    public void testFindByName() {
        assertEquals(1, dictServiceTypeDao.findByName("first", 0, 25).size());
    }

    @Test
    @Transactional
    public void testFindByClient1() {
        Client client = clientDao.findFullById(1L);
        assertEquals(2, dictServiceTypeDao.findByClient(client).size());
    }

    @Test
    @Transactional
    public void testFindByClient2() {
        Client client = clientDao.findFullById(2L);
        assertEquals(3, dictServiceTypeDao.findByClient(client).size());
    }

}
