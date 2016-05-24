package ru.urvanov.keystore.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.FileInputStream;
import java.math.BigInteger;
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
import ru.urvanov.keystore.dao.DictClientGroupDao;
import ru.urvanov.keystore.domain.Client;
import ru.urvanov.keystore.domain.ClientListItem;
import ru.urvanov.keystore.domain.ClientListParameters;
import ru.urvanov.keystore.domain.ClientType;
import ru.urvanov.keystore.domain.KeyActivationMode;
import ru.urvanov.keystore.test.SpringTestBase;

public class ClientDaoImplTest extends SpringTestBase {

    @Autowired
    private ClientDao clientDao;

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
        IDataSet dataSet = new FlatXmlDataSetBuilder().build(new FileInputStream("src/test/java/ru/urvanov/keystore/dao/ClientDaoImplTest.xml"));

        try (Connection connection = dataSource.getConnection())
        {
            databaseConnection = new DatabaseConnection(connection);
            DatabaseOperation.CLEAN_INSERT.execute(databaseConnection, dataSet);
        }
    }
    
    @Test
    @Transactional
    public void testFindById1() {
        Client client = clientDao.findById(1L);
        assertNotNull(client);
        assertEquals("client name", client.getName());
    }

    @Test
    @Transactional
    public void testFindById2() {
        Client client = clientDao.findById(-1L);
        assertNull(client);
    }

    @Test
    @Transactional
    public void testFindFullById() {
        Client client = clientDao.findFullById(2L);
        assertNotNull(client);
        assertEquals(1, client.getLinkClientDictServiceTypes().size());
    }

    @Test
    @Transactional
    public void testGetReference() {
        Client client = clientDao.getReference(1L);
        assertNotNull(client);
    }

    @Test
    @Transactional
    public void testSave() {
        Client client = new Client();
        client.setActive(true);
        client.setClientType(ClientType.INDIVIDUAL_ENTREPRENEUR);
        client.setContactPersonEmail("email@email.ru");
        client.setContactPersonName("contact person name");
        client.setContactPersonPhone("8908000000000");
        client.setDictClientGroup(dictClientGroupDao.getReference(1L));
        client.setIec("000000000");
        client.setItn("0000000000");
        client.setKeyActivationMode(KeyActivationMode.AUTOMATIC);
        client.setName("client name");
        client.setUniqueId("asdfasdf");
        client.setJuridicalPersonName("juridical person name");
        client.setPassword("blabla");
        client.setAuthority("ROLE_CLIENT");
        clientDao.save(client);
        
        Long id = client.getId();
        assertNotNull(id);
        client = clientDao.findById(id);
        assertNotNull(client.getLinkClientDictServiceTypes());
        assertEquals("client name", client.getName());
        assertEquals("0000000000", client.getItn());
        assertEquals("000000000", client.getIec());
        assertEquals(KeyActivationMode.AUTOMATIC, client.getKeyActivationMode());
        assertNotNull(client.getDictClientGroup());
        assertEquals(new Long(1L), client.getDictClientGroup().getId());
        assertEquals(ClientType.INDIVIDUAL_ENTREPRENEUR, client.getClientType());
    }

    @Test
    @Transactional
    public void testList() {
        ClientListParameters params = new ClientListParameters();
        params.setUserId(1L);
        List<ClientListItem> lst = clientDao.list(params);
        assertEquals(3, lst.size());
    }

    @Test
    @Transactional
    public void testCountList() {
        ClientListParameters params = new ClientListParameters();
        params.setUserId(1L);
        assertEquals(new BigInteger("3"), clientDao.countList(params));
    }

    @Test
    @Transactional
    public void testCheckClientNameUnique() {
        Client client = new Client();
        client.setName("client name");
        assertEquals(false, clientDao.checkNameUnique(client));
    }

    @Test
    @Transactional
    public void testCheckClientNameUnique2() {
        Client client = new Client();
        client.setName("asdf");
        assertEquals(true, clientDao.checkNameUnique(client));
    }

    @Test
    @Transactional
    public void testCheckClientNameUnique3() {
        Client client = clientDao.findById(1L);
        assertEquals(true, clientDao.checkNameUnique(client));
    }

    @Test
    @Transactional
    public void testFindByUniqueId1() {
        assertNotNull(clientDao.findByUniqueId("DF4344"));
    }
    
}
