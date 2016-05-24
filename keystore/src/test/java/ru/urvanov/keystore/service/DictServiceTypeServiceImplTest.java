package ru.urvanov.keystore.service;

import static org.junit.Assert.assertNotNull;

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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import ru.urvanov.keystore.dao.ClientDao;
import ru.urvanov.keystore.domain.DictServiceType;
import ru.urvanov.keystore.domain.UserAccessCode;
import ru.urvanov.keystore.domain.UserDetailsImpl;
import ru.urvanov.keystore.test.SpringTestBase;

public class DictServiceTypeServiceImplTest extends SpringTestBase {
    @Autowired
    private DictServiceTypeService dictServiceTypeService;

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
        IDataSet dataSet = new FlatXmlDataSetBuilder().build(new FileInputStream("src/test/java/ru/urvanov/keystore/service/DictServiceTypeServiceImplTest.xml"));

        try (Connection connection = dataSource.getConnection())
        {
            databaseConnection = new DatabaseConnection(connection);
            DatabaseOperation.CLEAN_INSERT.execute(databaseConnection, dataSet);
        }
    }
    
    @Transactional
    @Test
    public void testFindByIdWithClient1() {
        prepareSecurity("firstUserName");
        dictServiceTypeService.findById(1L);
    }

    @Transactional
    @Test
    public void testFindByIdWithService1() {
        prepareSecurity("service@nowhere.com");
        assertNotNull(dictServiceTypeService.findById(1L));
    }
    
    @Transactional
    @Test
    public void testGetReferenceWithClient1() {
        prepareSecurity("firstUserName");
        dictServiceTypeService.getReference(1L);
    }
    @Transactional
    @Test
    public void testGetReferenceWithService1() {
        prepareSecurity("service@nowhere.com");
        assertNotNull(dictServiceTypeService.getReference(1L));
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testFindAllWithClient1() {
        prepareSecurity("firstUserName");
        dictServiceTypeService.findAll();
    }

    @Transactional
    @Test
    public void testFindAllWithService1() {
        prepareSecurity("service@nowhere.com");
        assertNotNull(dictServiceTypeService.findAll());
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testSaveWithClient1() {
        prepareSecurity("firstUserName");
        dictServiceTypeService.save(new DictServiceType());
    }

    @Transactional
    @Test
    public void testSaveWithService1() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_SETTING_DICT_SERVICE_TYPE, true);
        dictServiceTypeService.save(dictServiceTypeService.findById(1L));
    }
    
    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testSaveWithService2() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_SETTING_DICT_SERVICE_TYPE, false);
        dictServiceTypeService.save(dictServiceTypeService.findById(1L));
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testFindByNameWithClient1() {
        prepareSecurity("firstUserName");
        dictServiceTypeService.findByName("first", 0, 25);
    }

    @Transactional
    @Test
    public void testFindByNameWithService1() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_SETTING_DICT_SERVICE_TYPE, true);
        assertNotNull(dictServiceTypeService.findByName("first", 0, 25));
    }
    
    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testFindByNameWithService2() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_SETTING_DICT_SERVICE_TYPE, false);
        dictServiceTypeService.findByName("first", 0, 25);
    }

    @Transactional
    @Test
    public void testFindByClientWithClient1() {
        prepareSecurity("firstUserName");
        assertNotNull(dictServiceTypeService.findByClient(clientDao
                .findFullById(((UserDetailsImpl) SecurityContextHolder
                        .getContext().getAuthentication().getPrincipal())
                        .getUser().getClient().getId())));
    }

    @Transactional
    @Test
    public void testFindByClientWithClient2() {
        prepareSecurity("service@nowhere.com");
        assertNotNull(dictServiceTypeService.findByClient(clientDao
                .findFullById(((UserDetailsImpl) SecurityContextHolder
                        .getContext().getAuthentication().getPrincipal())
                        .getUser().getClient().getId())));
    }
}
