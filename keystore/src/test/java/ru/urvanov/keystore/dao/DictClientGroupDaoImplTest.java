package ru.urvanov.keystore.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.FileInputStream;
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

import ru.urvanov.keystore.dao.DictClientGroupDao;
import ru.urvanov.keystore.domain.DictClientGroup;
import ru.urvanov.keystore.test.SpringTestBase;

public class DictClientGroupDaoImplTest extends SpringTestBase {

    @Autowired
    private DictClientGroupDao dictClientGroupDao;
    
    @Autowired
    private DataSource dataSource;
    
    @Before
    public void before() throws Exception {

        // initialize your database connection here
        IDatabaseConnection databaseConnection = null;
        // ...

        // initialize your dataset here
        IDataSet dataSet = new FlatXmlDataSetBuilder().build(new FileInputStream("src/test/java/ru/urvanov/keystore/dao/DictClientGroupDaoImplTest.xml"));

        try (Connection connection = dataSource.getConnection())
        {
            databaseConnection = new DatabaseConnection(connection);
            DatabaseOperation.CLEAN_INSERT.execute(databaseConnection, dataSet);
        }
    }
    
    @Test
    @Transactional
    public void test() {
        List<DictClientGroup> result = dictClientGroupDao.findAll();
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    @Transactional
    public void testFindById() {
        DictClientGroup dcg = dictClientGroupDao.findById(2L);
        assertNotNull(dcg);
        assertEquals("first dict client group", dcg.getName());
    }

    @Test
    @Transactional
    public void testFindAllRoot() {
        List<DictClientGroup> result = dictClientGroupDao.findAllRoot();
        assertEquals(2, result.size());
    }

    @Test
    @Transactional
    public void testSave() {
        DictClientGroup dcg = new DictClientGroup();
        dcg.setName("new dict client group");
        dictClientGroupDao.save(dcg);
        Long id = dcg.getId();
        assertNotNull(id);
        dcg = dictClientGroupDao.findById(id);
        assertNotNull(dcg);
        assertEquals("new dict client group", dcg.getName());
    }

}
