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

import ru.urvanov.keystore.domain.GlobalSettings;
import ru.urvanov.keystore.test.SpringTestBase;

public class GlobalSettingsServiceImplTest extends SpringTestBase {
    @Autowired
    private GlobalSettingsService globalSettingsService;

    @Autowired
    private DataSource dataSource;
    
    @Before
    public void before() throws Exception {

        // initialize your database connection here
        IDatabaseConnection databaseConnection = null;
        // ...

        // initialize your dataset here
        IDataSet dataSet = new FlatXmlDataSetBuilder().build(new FileInputStream("src/test/java/ru/urvanov/keystore/service/GlobalSettingsServiceImplTest.xml"));

        try (Connection connection = dataSource.getConnection())
        {
            databaseConnection = new DatabaseConnection(connection);
            DatabaseOperation.CLEAN_INSERT.execute(databaseConnection, dataSet);
        }
    }
    
    @Transactional
    @Test
    public void testReadWithClient1() {
        prepareSecurity("firstUserName");
        globalSettingsService.read();
    }

    @Transactional
    @Test
    public void testReadWithService1() {
        prepareSecurity("service@nowhere.com");
        globalSettingsService.read();
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testSaveWithClient1() {
        prepareSecurity("firstUserName");
        globalSettingsService.save(new GlobalSettings());
    }

    @Transactional
    @Test
    public void testSaveWithService1() {
        prepareSecurity("service@nowhere.com");
        globalSettingsService.save(globalSettingsService.read());
    }
}
