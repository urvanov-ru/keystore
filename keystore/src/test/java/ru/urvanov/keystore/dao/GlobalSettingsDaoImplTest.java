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

import ru.urvanov.keystore.dao.GlobalSettingsDao;
import ru.urvanov.keystore.domain.GlobalSettings;
import ru.urvanov.keystore.test.SpringTestBase;

public class GlobalSettingsDaoImplTest extends SpringTestBase {
    
    @Autowired
    private GlobalSettingsDao globalSettingsDao;
    
    @Autowired
    private DataSource dataSource;
    
    @Before
    public void before() throws Exception {

        // initialize your database connection here
        IDatabaseConnection databaseConnection = null;
        // ...

        // initialize your dataset here
        IDataSet dataSet = new FlatXmlDataSetBuilder().build(new FileInputStream("src/test/java/ru/urvanov/keystore/dao/GlobalSettingsDaoImplTest.xml"));

        try (Connection connection = dataSource.getConnection())
        {
            databaseConnection = new DatabaseConnection(connection);
            DatabaseOperation.CLEAN_INSERT.execute(databaseConnection, dataSet);
        }
    }
    
    @Test
    @Transactional
    public void testFindById1() {
        GlobalSettings globalSettings  = globalSettingsDao.findById(1L);
        assertNotNull(globalSettings);
    }
    
    @Test
    @Transactional
    public void testFindById2() {
        GlobalSettings globalSettings  = globalSettingsDao.findById(-1L);
        assertNull(globalSettings);
    }
    
    @Test
    @Transactional
    public void testSave() {
        GlobalSettings globalSettings = globalSettingsDao.findById(1L);
        globalSettings.setSessionStoreDays(10);
        globalSettingsDao.save(globalSettings);
        globalSettings = globalSettingsDao.findById(1L);
        assertEquals(new Integer(10), globalSettings.getSessionStoreDays());
    }

}
