package ru.urvanov.keystore.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.FileInputStream;
import java.math.BigInteger;
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
import ru.urvanov.keystore.dao.DictClientGroupDao;
import ru.urvanov.keystore.domain.Client;
import ru.urvanov.keystore.domain.ClientListParameters;
import ru.urvanov.keystore.domain.ClientType;
import ru.urvanov.keystore.domain.KeyActivationMode;
import ru.urvanov.keystore.domain.UserAccessCode;
import ru.urvanov.keystore.domain.UserDetailsImpl;
import ru.urvanov.keystore.exception.ClientNameNotUniqueException;
import ru.urvanov.keystore.exception.UserNameNotUniqueException;
import ru.urvanov.keystore.test.SpringTestBase;

public class ClientServiceImplTest extends SpringTestBase {

    @Autowired
    private ClientService clientService;

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
        IDataSet dataSet = new FlatXmlDataSetBuilder().build(new FileInputStream("src/test/java/ru/urvanov/keystore/service/ClientServiceImplTest.xml"));

        try (Connection connection = dataSource.getConnection())
        {
            databaseConnection = new DatabaseConnection(connection);
            DatabaseOperation.CLEAN_INSERT.execute(databaseConnection, dataSet);
        }
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testBlockByIdWithClient1() {
        prepareSecurity("firstUserName");
        clientService.blockById(1L);
    }

    @Transactional
    @Test
    public void testBlockByIdWithService1() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_ADMIN_CLIENT, true);
        clientService.blockById(1L);
        Client client = clientService.findById(1L);
        assertEquals(false, client.getActive());
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testBlockByIdWithService2() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_ADMIN_CLIENT, false);
        clientService.blockById(1L);
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testBlockByIdWithService3() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_ADMIN_CLIENT, true);
        clientService.blockById(3L);
    }

    @Transactional
    @Test
    public void testFindByIdWithClient1() {
        prepareSecurity("firstUserName");
        prepareUserAccess(UserAccessCode.CLIENT_ADMIN_CLIENT_ACCOUNT, true);
        assertNotNull(clientService.findById(1L));
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testFindByIdWithClient2() {
        prepareSecurity("firstUserName");
        prepareUserAccess(UserAccessCode.CLIENT_ADMIN_CLIENT_ACCOUNT, false);
        clientService.findById(2L);
    }

    @Transactional
    @Test
    public void testFindByIdWithService1() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_ADMIN_ACCOUNT, true);
        assertNotNull(clientService.findById(3L));
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testFindByIdWithService2() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_ADMIN_ACCOUNT, false);
        assertNotNull(clientService.findById(3L));
    }

    @Transactional
    @Test
    public void testFindByIdWithService3() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_ADMIN_CLIENT, true);
        assertNotNull(clientService.findById(1L));
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testFindByIdWithService4() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(new UserAccessCode[] {
                UserAccessCode.SERVICE_READ_CLIENT,
                UserAccessCode.SERVICE_ADMIN_CLIENT,
                UserAccessCode.SERVICE_NEW_CLIENT_NOTIFICATION,
                UserAccessCode.SERVICE_ADD_CLIENT,
                UserAccessCode.SERVICE_EXPORT_CLIENT }, false);
        assertNotNull(clientService.findById(1L));
    }

    @Transactional
    @Test
    public void testFindByIdWithService5() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_NEW_CLIENT_NOTIFICATION, true);
        assertNotNull(clientService.findById(1L));
    }

    @Transactional
    @Test
    public void testFindByIdWithService6() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_READ_CLIENT, true);
        assertNotNull(clientService.findById(1L));
    }

    @Transactional
    @Test
    public void testFindByIdWithService7() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_ADD_CLIENT, false);
        assertNotNull(clientService.findById(1L));
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testFindByIdWithService8() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_READ_CLIENT, true);
        assertNotNull(clientService.findById(3L));
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testFindByIdWithService9() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_ADMIN_CLIENT, true);
        assertNotNull(clientService.findById(3L));
    }

    @Transactional
    @Test
    public void testFindByIdWithService10() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_EXPORT_CLIENT, true);
        assertNotNull(clientService.findById(1L));
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testFindByIdWithService11() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_EXPORT_CLIENT, true);
        assertNotNull(clientService.findById(3L));
    }

    @Transactional
    @Test
    public void testFindFullByIdWithClient1() {
        prepareSecurity("firstUserName");
        prepareUserAccess(UserAccessCode.CLIENT_ADMIN_CLIENT_ACCOUNT, true);
        assertNotNull(clientService.findFullById(1L));
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testFindFullByIdWithClient2() {
        prepareSecurity("firstUserName");
        prepareUserAccess(UserAccessCode.CLIENT_ADMIN_CLIENT_ACCOUNT, false);
        clientService.findById(2L);
    }

    @Transactional
    @Test
    public void testFindFullByIdWithService1() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_ADMIN_ACCOUNT, true);
        assertNotNull(clientService.findFullById(3L));
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testFindFullByIdWithService2() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_ADMIN_ACCOUNT, false);
        assertNotNull(clientService.findFullById(3L));
    }

    @Transactional
    @Test
    public void testFindFullByIdWithService3() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_ADMIN_CLIENT, true);
        assertNotNull(clientService.findFullById(1L));
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testFindFullByIdWithService4() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(new UserAccessCode[] {
                UserAccessCode.SERVICE_ADMIN_CLIENT,
                UserAccessCode.SERVICE_READ_CLIENT,
                UserAccessCode.SERVICE_NEW_CLIENT_NOTIFICATION,
                UserAccessCode.SERVICE_ADD_CLIENT,
                UserAccessCode.SERVICE_EXPORT_CLIENT }, false);
        assertNotNull(clientService.findFullById(1L));
    }

    @Transactional
    @Test
    public void testFindFullByIdWithService5() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_NEW_CLIENT_NOTIFICATION, true);
        assertNotNull(clientService.findFullById(1L));
    }

    @Transactional
    @Test
    public void testFindFullByIdWithService6() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_READ_CLIENT, true);
        assertNotNull(clientService.findFullById(1L));
    }

    @Transactional
    @Test
    public void testFindFullByIdWithService7() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_ADD_CLIENT, true);
        assertNotNull(clientService.findFullById(1L));
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testFindFullByIdWithService8() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_READ_CLIENT, true);
        assertNotNull(clientService.findFullById(3L));
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testFindFullByIdWithService9() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_ADMIN_CLIENT, true);
        assertNotNull(clientService.findFullById(3L));
    }

    @Transactional
    @Test
    public void testFindFullByIdWithService10() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_EXPORT_CLIENT, true);
        assertNotNull(clientService.findFullById(1L));
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testFindFullByIdWithService11() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_EXPORT_CLIENT, true);
        assertNotNull(clientService.findFullById(3L));
    }

    @Transactional
    @Test
    public void testGetReferenceWithService1() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_ADMIN_ACCOUNT, true);
        assertNotNull(clientService.getReference(3L));
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testGetReferenceWithService2() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_ADMIN_ACCOUNT, false);
        assertNotNull(clientService.getReference(3L));
    }

    @Transactional
    @Test
    public void testGetReferenceWithService3() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_ADMIN_CLIENT, true);
        assertNotNull(clientService.getReference(1L));
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testGetReferenceWithService4() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(new UserAccessCode[] {
                UserAccessCode.SERVICE_READ_CLIENT,
                UserAccessCode.SERVICE_ADMIN_CLIENT,
                UserAccessCode.SERVICE_NEW_CLIENT_NOTIFICATION,
                UserAccessCode.SERVICE_ADD_CLIENT,
                UserAccessCode.SERVICE_EXPORT_CLIENT }, false);
        assertNotNull(clientService.getReference(1L));
    }

    @Transactional
    @Test
    public void testGetReferenceWithService5() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_NEW_CLIENT_NOTIFICATION, true);
        assertNotNull(clientService.getReference(1L));
    }

    @Transactional
    @Test
    public void testGetReferenceWithService6() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_READ_CLIENT, true);
        assertNotNull(clientService.getReference(1L));
    }

    @Transactional
    @Test
    public void testGetReferenceWithService7() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_ADD_CLIENT, false);
        assertNotNull(clientService.getReference(1L));
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testGetReferenceWithService8() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_READ_CLIENT, true);
        assertNotNull(clientService.getReference(3L));
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testGetReferenceWithService9() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_ADMIN_CLIENT, true);
        assertNotNull(clientService.getReference(3L));
    }

    @Transactional
    @Test
    public void testGetReferenceWithService10() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_EXPORT_CLIENT, true);
        assertNotNull(clientService.getReference(1L));
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testGetReferenceWithService11() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_EXPORT_CLIENT, true);
        assertNotNull(clientService.getReference(3L));
    }

    @Transactional
    @Test
    public void testSaveWithClient1() throws ClientNameNotUniqueException,
            UserNameNotUniqueException {
        prepareSecurity("firstUserName");
        prepareUserAccess(UserAccessCode.CLIENT_ADMIN_CLIENT_ACCOUNT, true);
        Client client = clientDao.findById(1L);
        client.setContactPersonName("FFF");
        clientService.save(client);
        client = clientDao.findById(1L);
        assertEquals("FFF", client.getContactPersonName());
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testSaveWithClient2() throws ClientNameNotUniqueException,
            UserNameNotUniqueException {
        prepareSecurity("firstUserName");
        prepareUserAccess(UserAccessCode.CLIENT_ADMIN_CLIENT_ACCOUNT, false);
        Client client = clientDao.findById(1L);
        client.setContactPersonName("FFF");
        clientService.save(client);
        client = clientDao.findById(1L);
        assertEquals("FFF", client.getContactPersonName());
    }

    @Transactional
    @Test
    public void testSaveAnonymous() throws ClientNameNotUniqueException,
            UserNameNotUniqueException {
        prepareAnonymousSecurity();
        Client client = new Client();
        client.setName("asdg");
        client.setClientType(ClientType.INDIVIDUAL_ENTREPRENEUR);
        client.setActive(true);
        client.setContactPersonEmail("contactPersonEmail");
        client.setUniqueId("uniqueId");
        client.setDictClientGroup(dictClientGroupDao.findById(1L));
        client.setContactPersonPhone("contactPersonPhone");
        client.setKeyActivationMode(KeyActivationMode.AUTOMATIC);
        client.setAuthority("ROLE_CLIENT");
        client.setPassword("password");
        client.setContactPersonName("FFF");
        clientService.save(client);
        client = clientDao.findById(client.getId());
        assertEquals("FFF", client.getContactPersonName());
    }

    @Transactional
    @Test
    public void testSaveWithService1() throws ClientNameNotUniqueException,
            UserNameNotUniqueException {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_ADMIN_ACCOUNT, true);
        Client client = clientDao.findById(3L);
        client.setContactPersonName("FFF");
        clientService.save(client);
        client = clientDao.findById(3L);
        assertEquals("FFF", client.getContactPersonName());
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testSaveWithService2() throws ClientNameNotUniqueException,
            UserNameNotUniqueException {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_ADMIN_ACCOUNT, false);
        Client client = clientDao.findById(3L);
        client.setContactPersonName("FFF");
        clientService.save(client);
        client = clientDao.findById(3L);
        assertEquals("FFF", client.getContactPersonName());
    }

    @Transactional
    @Test
    public void testSaveWithService3() throws ClientNameNotUniqueException,
            UserNameNotUniqueException {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_ADMIN_CLIENT, true);
        Client client = clientDao.findById(1L);
        client.setContactPersonName("FFF");
        clientService.save(client);
        client = clientDao.findById(1L);
        assertEquals("FFF", client.getContactPersonName());
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testSaveWithService4() throws ClientNameNotUniqueException,
            UserNameNotUniqueException {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_ADMIN_CLIENT, false);
        Client client = clientDao.findById(1L);
        client.setContactPersonName("FFF");
        clientService.save(client);
        client = clientDao.findById(1L);
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testSaveWithService5() throws ClientNameNotUniqueException,
            UserNameNotUniqueException {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_ADD_CLIENT, true);
        Client client = clientDao.findById(1L);
        client.setContactPersonName("FFF");
        clientService.save(client);
        client = clientDao.findById(1L);
    }

    @Transactional
    @Test
    public void testSaveWithService6() throws ClientNameNotUniqueException,
            UserNameNotUniqueException {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_ADD_CLIENT, true);
        Client client = new Client();
        client.setActive(true);
        client.setClientType(ClientType.INDIVIDUAL_ENTREPRENEUR);
        client.setContactPersonEmail("contactpersonemail");
        client.setContactPersonPhone("contactPersonPhone");
        client.setContactPersonName("FFF");
        client.setDictClientGroup(dictClientGroupDao.findById(1L));
        client.setKeyActivationMode(KeyActivationMode.AUTOMATIC);
        client.setJuridicalPersonName("juridicalPersonName");
        client.setName("nameasdfadfh43");
        client.setPassword("PPPP");
        client.setUniqueId("azsli4uthoaw4");
        client.setAuthority("ROLE_CLIENT");
        clientService.save(client);
        assertNotNull(client.getId());
    }
    
    @Transactional
    @Test
    public void testSaveWithService7() throws ClientNameNotUniqueException,
            UserNameNotUniqueException {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(new UserAccessCode[]{UserAccessCode.SERVICE_ADD_CLIENT,
                                             UserAccessCode.SERVICE_ADMIN_CLIENT}, true);
        Client client = new Client();
        client.setActive(true);
        client.setClientType(ClientType.INDIVIDUAL_ENTREPRENEUR);
        client.setContactPersonEmail("contactpersonemail");
        client.setContactPersonPhone("contactPersonPhone");
        client.setContactPersonName("FFF");
        client.setDictClientGroup(dictClientGroupDao.findById(1L));
        client.setKeyActivationMode(KeyActivationMode.AUTOMATIC);
        client.setJuridicalPersonName("juridicalPersonName");
        client.setName("nameasdfadfh43");
        client.setPassword("PPPP");
        client.setUniqueId("azsli4uthoaw4");
        client.setAuthority("ROLE_CLIENT");
        clientService.save(client);
        assertNotNull(client.getId());
    }

    @Transactional
    @Test
    public void testSave2WithService1() throws ClientNameNotUniqueException,
            UserNameNotUniqueException {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_ADMIN_ACCOUNT, true);
        Client client = clientDao.findById(3L);
        client.setContactPersonName("FFF");
        clientService.save(client, null);
        client = clientDao.findById(3L);
        assertEquals("FFF", client.getContactPersonName());
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testSave2WithService2() throws ClientNameNotUniqueException,
            UserNameNotUniqueException {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_ADMIN_ACCOUNT, false);
        Client client = clientDao.findById(3L);
        client.setContactPersonName("FFF");
        clientService.save(client, null);
        client = clientDao.findById(3L);
        assertEquals("FFF", client.getContactPersonName());
    }

    @Transactional
    @Test
    public void testSave2WithService3() throws ClientNameNotUniqueException,
            UserNameNotUniqueException {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_ADMIN_CLIENT, true);
        Client client = clientDao.findById(1L);
        client.setContactPersonName("FFF");
        clientService.save(client, null);
        client = clientDao.findById(1L);
        assertEquals("FFF", client.getContactPersonName());
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testSave2WithService4() throws ClientNameNotUniqueException,
            UserNameNotUniqueException {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_ADMIN_CLIENT, false);
        Client client = clientDao.findById(1L);
        client.setContactPersonName("FFF");
        clientService.save(client, null);
        client = clientDao.findById(1L);
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testSave2WithService5() throws ClientNameNotUniqueException,
            UserNameNotUniqueException {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_ADD_CLIENT, true);
        Client client = clientDao.findById(1L);
        client.setContactPersonName("FFF");
        clientService.save(client, null);
        client = clientDao.findById(1L);
    }

    @Transactional
    @Test
    public void testSave2WithService6() throws ClientNameNotUniqueException,
            UserNameNotUniqueException {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_ADD_CLIENT, true);
        Client client = new Client();
        client.setActive(true);
        client.setClientType(ClientType.INDIVIDUAL_ENTREPRENEUR);
        client.setContactPersonEmail("contactpersonemail");
        client.setContactPersonPhone("contactPersonPhone");
        client.setContactPersonName("FFF");
        client.setDictClientGroup(dictClientGroupDao.findById(1L));
        client.setKeyActivationMode(KeyActivationMode.AUTOMATIC);
        client.setJuridicalPersonName("juridicalPersonName");
        client.setName("nameasdfadfh43");
        client.setPassword("PPPP");
        client.setUniqueId("azsli4uthoaw4");
        client.setAuthority("ROLE_CLIENT");
        clientService.save(client, null);
        assertNotNull(client.getId());
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testListWithClient1() {
        prepareSecurity("firstUserName");
        clientService.list(new ClientListParameters());
    }

    @Transactional
    @Test
    public void testListWithService1() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_READ_CLIENT, true);
        ClientListParameters params = new ClientListParameters();
        params.setUserId(((UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal()).getUser().getId());
        assertNotNull(clientService.list(params));
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testListWithService2() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(new UserAccessCode[] {
                UserAccessCode.SERVICE_ADMIN_CLIENT,
                UserAccessCode.SERVICE_NEW_CLIENT_NOTIFICATION,
                UserAccessCode.SERVICE_READ_CLIENT,
                UserAccessCode.SERVICE_ADD_CLIENT,
                UserAccessCode.SERVICE_EXPORT_CLIENT }, false);
        ClientListParameters params = new ClientListParameters();
        params.setUserId(((UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal()).getUser().getId());
        clientService.list(params);
    }

    @Transactional
    @Test
    public void testListWithService3() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_ADMIN_CLIENT, true);
        ClientListParameters params = new ClientListParameters();
        params.setUserId(((UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal()).getUser().getId());
        assertNotNull(clientService.list(params));
    }

    @Transactional
    @Test
    public void testListWithService4() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_NEW_CLIENT_NOTIFICATION, true);
        ClientListParameters params = new ClientListParameters();
        params.setUserId(((UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal()).getUser().getId());
        assertNotNull(clientService.list(params));
    }

    @Transactional
    @Test
    public void testListWithService5() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_ADD_CLIENT, true);
        ClientListParameters params = new ClientListParameters();
        params.setUserId(((UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal()).getUser().getId());
        assertNotNull(clientService.list(params));
    }

    @Transactional
    @Test
    public void testListWithService6() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_EXPORT_CLIENT, true);
        ClientListParameters params = new ClientListParameters();
        params.setUserId(((UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal()).getUser().getId());
        assertNotNull(clientService.list(params));
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testCountListWithClient1() {
        prepareSecurity("firstUserName");
        clientService.countList(new ClientListParameters());
    }

    @Transactional
    @Test
    public void testCountListWithService1() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_READ_CLIENT, true);
        ClientListParameters params = new ClientListParameters();
        params.setUserId(((UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal()).getUser().getId());
        assertNotEquals(new BigInteger("0"), clientService.countList(params));
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testCountListWithService2() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(new UserAccessCode[] {
                UserAccessCode.SERVICE_ADMIN_CLIENT,
                UserAccessCode.SERVICE_NEW_CLIENT_NOTIFICATION,
                UserAccessCode.SERVICE_READ_CLIENT,
                UserAccessCode.SERVICE_ADD_CLIENT,
                UserAccessCode.SERVICE_EXPORT_CLIENT }, false);
        ClientListParameters params = new ClientListParameters();
        params.setUserId(((UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal()).getUser().getId());
        clientService.countList(params);
    }

    @Transactional
    @Test
    public void testCountListWithService3() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_ADMIN_CLIENT, true);
        ClientListParameters params = new ClientListParameters();
        params.setUserId(((UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal()).getUser().getId());
        assertNotEquals(new BigInteger("0"), clientService.countList(params));
    }

    @Transactional
    @Test
    public void testCountListWithService4() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_NEW_CLIENT_NOTIFICATION, true);
        ClientListParameters params = new ClientListParameters();
        params.setUserId(((UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal()).getUser().getId());
        assertNotEquals(new BigInteger("0"), clientService.countList(params));
    }

    @Transactional
    @Test
    public void testCountListWithService5() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_ADD_CLIENT, true);
        ClientListParameters params = new ClientListParameters();
        params.setUserId(((UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal()).getUser().getId());
        assertNotEquals(new BigInteger("0"), clientService.countList(params));
    }

    @Transactional
    @Test
    public void testCountListWithService6() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_EXPORT_CLIENT, true);
        ClientListParameters params = new ClientListParameters();
        params.setUserId(((UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal()).getUser().getId());
        assertNotEquals(new BigInteger("0"), clientService.countList(params));
    }
    
    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testGiveDictServiceTypeWithClient1() {
        prepareSecurity("firstUserName");
        clientService.giveDictServiceType(clientDao.findById(1L), 1L);
    }
    
    
    @Transactional
    @Test
    public void testGiveDictServiceTypeWithService1() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_ADMIN_CLIENT, true);
        clientService.giveDictServiceType(clientDao.findById(1L), 1L);
    }
    
    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testGiveDictServiceTypeWithService2() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_ADMIN_CLIENT, false);
        clientService.giveDictServiceType(clientDao.findById(1L), 1L);
    }
    
    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void removeGiveDictServiceTypeWithClient1() {
        prepareSecurity("firstUserName");
        clientService.removeDictServiceType(clientDao.findById(2L), 3L);
    }
    
    @Transactional
    @Test
    public void removeGiveDictServiceTypeWithService1() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_ADMIN_CLIENT, true);
        clientService.removeDictServiceType(clientDao.findById(2L), 3L);
    }
    
    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void removeGiveDictServiceTypeWithService2() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_ADMIN_CLIENT, false);
        clientService.removeDictServiceType(clientDao.findById(2L), 3L);
    }
    
    @Transactional
    @Test
    public void testFindService() {
        assertNotNull(clientService.findService());
    }
    
    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testGiveDictActionWithClient1() {
        prepareSecurity("firstUserName");
        clientService.giveDictAction(clientDao.findById(1L), 1L);
    }
    
    @Transactional
    @Test
    public void testGiveDictActionWithService1() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_ADMIN_CLIENT, true);
        clientService.giveDictAction(clientDao.findById(1L), 1L);
    }
    
    @Transactional
    @Test
    public void testGiveDictActionWithService2() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_ADMIN_CLIENT, true);
        clientService.giveDictAction(clientDao.findById(1L), 1L);
    }
    
    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void removeDictActionWithClient1() {
        prepareSecurity("firstUserName");
        clientService.removeDictAction(clientDao.findById(2L), 3L);
    }
    
    @Transactional
    @Test
    public void removeDictActionWithService1() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_ADMIN_CLIENT, true);
        clientService.removeDictAction(clientDao.findById(2L), 3L);
    }
    
    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void removeDictActionWithService2() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_ADMIN_CLIENT, false);
        clientService.removeDictAction(clientDao.findById(2L), 3L);
    }
    
    @Transactional
    @Test
    public void testChangePasswordWithClient1() throws ClientNameNotUniqueException,
            UserNameNotUniqueException {
        prepareSecurity("firstUserName");
        prepareUserAccess(UserAccessCode.CLIENT_ADMIN_CLIENT_ACCOUNT, true);
        Client client = clientDao.findById(1L);
        clientService.changePassword(client, "1");
        client = clientDao.findById(1L);
        assertTrue(bcryptEncoder.matches("1", client.getPassword()));
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testChangePasswordWithClient2() throws ClientNameNotUniqueException,
            UserNameNotUniqueException {
        prepareSecurity("firstUserName");
        prepareUserAccess(UserAccessCode.CLIENT_ADMIN_CLIENT_ACCOUNT, false);
        Client client = clientDao.findById(1L);
        clientService.changePassword(client, "1");
    }
    
    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testChangePasswordWithClient3() throws ClientNameNotUniqueException,
            UserNameNotUniqueException {
        prepareSecurity("firstUserName");
        prepareUserAccess(UserAccessCode.CLIENT_ADMIN_CLIENT_ACCOUNT, true);
        Client client = clientDao.findById(2L);
        clientService.changePassword(client, "1");
    }
    
    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testChangePasswordWithService1() throws ClientNameNotUniqueException,
            UserNameNotUniqueException {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.CLIENT_ADMIN_CLIENT_ACCOUNT, true);
        Client client = clientDao.findById(3L);
        clientService.changePassword(client, "1");
    }
}
