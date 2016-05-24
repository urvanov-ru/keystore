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

import ru.urvanov.keystore.domain.UserAccessCode;
import ru.urvanov.keystore.test.SpringTestBase;

public class DictClientGroupServiceImplTest extends SpringTestBase {
    @Autowired
    private DictClientGroupService dictClientGroupService;

    @Autowired
    private DataSource dataSource;
    
    @Before
    public void before() throws Exception {

        // initialize your database connection here
        IDatabaseConnection databaseConnection = null;
        // ...

        // initialize your dataset here
        IDataSet dataSet = new FlatXmlDataSetBuilder().build(new FileInputStream("src/test/java/ru/urvanov/keystore/service/DictClientGroupServiceImplTest.xml"));

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
        dictClientGroupService.findById(1L);
    }

    @Transactional
    @Test
    public void testFindByIdWithService1() {
        prepareSecurity("service@nowhere.com");
        dictClientGroupService.findById(1L);
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testFindAllByClient1() {
        prepareSecurity("firstUserName");
        dictClientGroupService.findAll();
    }

    @Transactional
    @Test
    public void testFindAllByService1() {
        prepareSecurity("service@nowhere.com");
        dictClientGroupService.findAll();
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testSaveWithClient1() {
        prepareSecurity("firstUserName");
        dictClientGroupService.save(dictClientGroupService.findById(1L));
    }

    @Transactional
    @Test
    public void testSaveWithService1() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_SETTING_CLIENT_GROUP, true);
        dictClientGroupService.save(dictClientGroupService.findById(1L));
    }
    
    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testSaveWithService2() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_SETTING_CLIENT_GROUP, false);
        dictClientGroupService.save(dictClientGroupService.findById(1L));
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testFindAllRootWithClient1() {
        prepareSecurity("firstUserName");
        dictClientGroupService.findAllRoot();
    }

    @Transactional
    @Test
    public void testFindAllRootWithService1() {
        prepareSecurity("service@nowhere.com");
        dictClientGroupService.findAllRoot();
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testFindAllHierarchyWithClient1() {
        prepareSecurity("firstUserName");
        dictClientGroupService.findAllHierarchy();
    }

    @Transactional
    @Test
    public void testFindAllHierarchyWithService1() {
        prepareSecurity("service@nowhere.com");
        dictClientGroupService.findAllHierarchy();
    }
}
