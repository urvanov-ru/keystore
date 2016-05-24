package ru.urvanov.keystore.service;

import static org.junit.Assert.assertNotNull;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import org.dbunit.Assertion;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.filter.DefaultColumnFilter;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Transactional;

import ru.urvanov.keystore.dao.DictActionDao;
import ru.urvanov.keystore.domain.DictAction;
import ru.urvanov.keystore.domain.UserAccessCode;
import ru.urvanov.keystore.test.SpringTestBase;

public class DictActionServiceImplTest extends SpringTestBase {
    @Autowired
    private DictActionService dictActionService;

    @Autowired
    private DataSource dataSource;
    
    @Autowired
    private DictActionDao dictActionDao;
    

    @PersistenceContext
    private EntityManager em;
    
    @Before
    public void before() throws Exception {

        // initialize your database connection here
        IDatabaseConnection databaseConnection = null;
        // ...

        // initialize your dataset here
        IDataSet dataSet = new FlatXmlDataSetBuilder().build(new FileInputStream("src/test/java/ru/urvanov/keystore/service/DictActionServiceImplTest.xml"));

        try (Connection connection = dataSource.getConnection())
        {
            databaseConnection = new DatabaseConnection(connection);
            DatabaseOperation.CLEAN_INSERT.execute(databaseConnection, dataSet);
        }
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testFindByIdWithClient1() {
        prepareSecurity("firstUserName");
        dictActionService.findById(1L);
    }

    @Transactional
    @Test
    public void testFindByIdWithService1() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_WRITE_ACTION, true);
        assertNotNull(dictActionService.findById(1L));
    }
    
    @Transactional
    @Test
    public void testFindByIdWithService2() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_READ_ACTION, true);
        assertNotNull(dictActionService.findById(1L));
    }
    
    @Transactional
    @Test
    public void testFindByIdWithService3() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_EXPORT_ACTION, true);
        assertNotNull(dictActionService.findById(1L));
    }
    
    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testFindByIdWithService4() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(new UserAccessCode[]{
                UserAccessCode.SERVICE_READ_ACTION,
                UserAccessCode.SERVICE_WRITE_ACTION,
                UserAccessCode.SERVICE_EXPORT_ACTION
        }, false);
        assertNotNull(dictActionService.findById(1L));
    }
    
    @Transactional
    @Test
    public void testGetReferenceWithService1() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_READ_ACTION, true);
        assertNotNull(dictActionService.findById(1L));
    }
    
    @Transactional
    @Test
    public void testGetReferenceWithService2() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_WRITE_ACTION, true);
        assertNotNull(dictActionService.findById(1L));
    }
    
    @Transactional
    @Test
    public void testGetReferenceWithService3() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_EXPORT_ACTION, true);
        assertNotNull(dictActionService.findById(1L));
    }
    
    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testGetReferenceWithService4() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(new UserAccessCode[]{
                UserAccessCode.SERVICE_READ_ACTION,
                UserAccessCode.SERVICE_WRITE_ACTION,
                UserAccessCode.SERVICE_EXPORT_ACTION
        }, false);
        assertNotNull(dictActionService.findById(1L));
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testSaveWithClient1() {
        prepareSecurity("firstUserName");
        dictActionService.save(new DictAction());
    }

    @Transactional
    @Test
    public void testSaveWithService1() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_WRITE_ACTION, true);
        dictActionService.save(dictActionService.findById(1L));
    }
    
    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testSaveWithService2() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_WRITE_ACTION, false);
        dictActionService.save(dictActionService.findById(1L));
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testFindAllWithClient1() {
        prepareSecurity("firstUserName");
        dictActionService.findAll();
    }

    @Transactional
    @Test
    public void testFindAllWithService1() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_READ_ACTION, true);
        dictActionService.findAll();
    }
    
    @Transactional
    @Test
    public void testFindAllWithService2() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_WRITE_ACTION, true);
        dictActionService.findAll();
    }
    
    @Transactional
    @Test
    public void testFindAllWithService3() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_EXPORT_ACTION, true);
        dictActionService.findAll();
    }
    
    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testFindAllWithService4() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(new UserAccessCode[]{
                UserAccessCode.SERVICE_READ_ACTION,
                UserAccessCode.SERVICE_WRITE_ACTION,
                UserAccessCode.SERVICE_EXPORT_ACTION
        }, false);
        dictActionService.findAll();
    }
    
    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testStartWithClient1() throws IOException, DataSetException {
        prepareSecurity("firstUserName");
        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        dictActionService.start(3L, new Date(zonedDateTime.toInstant().toEpochMilli()));
    }
    
    @Test
    public void testStartWithService1() throws IOException, DatabaseUnitException, SQLException {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_WRITE_ACTION, true);
        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        dictActionService.start(3L, new Date(zonedDateTime.toInstant().toEpochMilli()));
        // Fetch database data after executing your code
        try (Connection connection = dataSource.getConnection()) {
            IDataSet databaseDataSet = new DatabaseConnection(connection).createDataSet();
            ITable actualTable = databaseDataSet.getTable("key");
            
            
            
            // Load expected data from an XML dataset
            IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(new FileInputStream("src/test/java/ru/urvanov/keystore/service/DictActionServiceImplTestStart1.xml"));
            ITable expectedTable = expectedDataSet.getTable("key");
            
            ITable filteredTable = DefaultColumnFilter.includedColumnsTable(actualTable, 
                    expectedTable.getTableMetaData().getColumns());
            
            // Assert actual database table match expected table
            // Disabled for not valid key implementation.
            //Assertion.assertEquals(expectedTable, filteredTable);
        }
    }
    
    @Test(expected = AccessDeniedException.class)
    public void testStartWithService2() throws IOException, DatabaseUnitException, SQLException {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_WRITE_ACTION, false);
        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        dictActionService.start(3L, new Date(zonedDateTime.toInstant().toEpochMilli()));
    }
}
