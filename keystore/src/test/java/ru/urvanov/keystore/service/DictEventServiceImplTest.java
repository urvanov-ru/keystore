package ru.urvanov.keystore.service;

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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Transactional;

import ru.urvanov.keystore.domain.DictEvent;
import ru.urvanov.keystore.domain.UserAccessCode;
import ru.urvanov.keystore.test.SpringTestBase;

public class DictEventServiceImplTest extends SpringTestBase {
    @Autowired
    private DictEventService dictEventService;

    @Autowired
    private DataSource dataSource;
    
    @Before
    public void before() throws Exception {

        // initialize your database connection here
        IDatabaseConnection databaseConnection = null;
        // ...

        // initialize your dataset here
        IDataSet dataSet = new FlatXmlDataSetBuilder().build(new FileInputStream("src/test/java/ru/urvanov/keystore/service/DictEventServiceImplTest.xml"));

        try (Connection connection = dataSource.getConnection())
        {
            databaseConnection = new DatabaseConnection(connection);
            DatabaseOperation.CLEAN_INSERT.execute(databaseConnection, dataSet);
        }
    }
    
    @Transactional
    public void testFindByIdWithClient1() {
        prepareSecurity("firstUserName");
        dictEventService.findById(1L);
    }

    @Transactional
    @Test
    public void testFindByIdWithService1() {
        prepareSecurity("service@nowhere.com");
        dictEventService.findById(1L);
    }

    @Transactional
    @Test
    public void testGetReferenceWithService1() {
        prepareSecurity("service@nowhere.com");
        dictEventService.getReference(1L);
    }

    @Transactional
    public void testFindAllWithClient1() {
        prepareSecurity("firstUserName");
        dictEventService.findAll();
    }

    @Transactional
    @Test
    public void testFindAllWithService1() {
        prepareSecurity("service@nowhere.com");
        dictEventService.findAll();
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testSaveWithClient1() {
        prepareSecurity("firstUserName");
        dictEventService.save(new DictEvent());
    }

    @Transactional
    @Test
    public void testSaveWithService1() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_SETTING_EMAIL_TEMPLATE, true);
        dictEventService.save(dictEventService.findById(1L));
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testSaveWithService2() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_SETTING_EMAIL_TEMPLATE, false);
        dictEventService.save(dictEventService.findById(1L));
    }
}
