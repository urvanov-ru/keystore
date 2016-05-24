package ru.urvanov.keystore.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import ru.urvanov.keystore.dao.ClientDao;
import ru.urvanov.keystore.dao.KeyDao;
import ru.urvanov.keystore.domain.ClientUserDetailsImpl;
import ru.urvanov.keystore.domain.Key;
import ru.urvanov.keystore.domain.KeyListParameters;
import ru.urvanov.keystore.domain.KeyStatus;
import ru.urvanov.keystore.domain.OrderStatus;
import ru.urvanov.keystore.domain.UserAccessCode;
import ru.urvanov.keystore.domain.UserDetailsImpl;
import ru.urvanov.keystore.test.SpringTestBase;

public class KeyServiceImplTest extends SpringTestBase {

    @Autowired
    private KeyService keyService;

    @Autowired
    private KeyDao keyDao;

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
        FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
        builder.setColumnSensing(true);
        IDataSet dataSet = builder
                .build(new FileInputStream(
                        "src/test/java/ru/urvanov/keystore/service/KeyServiceImplTest.xml"));

        try (Connection connection = dataSource.getConnection()) {
            databaseConnection = new DatabaseConnection(connection);
            DatabaseOperation.CLEAN_INSERT.execute(databaseConnection, dataSet);
        }
    }

    @Transactional
    @Test
    public void testFindByClientIdWithApiClient1() {
        prepareApiSecurity("FFFE");
        keyService
                .findByClientId(((ClientUserDetailsImpl) SecurityContextHolder
                        .getContext().getAuthentication().getPrincipal())
                        .getClient().getId());
    }

    @Transactional
    @Test
    public void testFindByIdWithClient1() {
        prepareSecurity("firstUserName");
        prepareUserAccess(UserAccessCode.CLIENT_READ_KEY, true);
        keyService.findById(1L);
    }

    @Transactional
    @Test
    public void testFindByIdWithClient2() {
        prepareSecurity("firstUserName");
        prepareUserAccess(UserAccessCode.CLIENT_ACTIVATE_KEY, true);
        keyService.findById(1L);
    }

    @Transactional
    @Test
    public void testFindByIdWithClient3() {
        prepareSecurity("firstUserName");
        prepareUserAccess(UserAccessCode.CLIENT_GET_KEY, true);
        keyService.findById(1L);
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testFindByIdWithClient4() {
        prepareSecurity("secondUserName");
        prepareUserAccess(UserAccessCode.CLIENT_READ_KEY, true);
        keyService.findById(1L);
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testFindByIdWithClient5() {
        prepareSecurity("firstUserName");
        prepareUserAccess(new UserAccessCode[] {
                UserAccessCode.CLIENT_READ_KEY,
                UserAccessCode.CLIENT_ACTIVATE_KEY,
                UserAccessCode.CLIENT_GET_KEY }, false);
        keyService.findById(1L);
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testSaveWithClient1() {
        prepareSecurity("firstUserName");
        Key key = keyService.findById(1L);
        key.setClient(clientDao.findById(2L));
        keyService.save(key);
    }

    @Transactional
    @Test
    public void testSaveWithClient2() {
        prepareSecurity("firstUserName");
        Key key = keyDao.findById(1L);
        keyService.save(key);
    }

    @Transactional
    @Test
    public void testSaveWithRolePay1() {
        prepareRolePaySecurity();
        Key key = keyDao.findById(1L);
        keyService.save(key);
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testSaveWithAnonymous1() {
        prepareAnonymousSecurity();
        Key key = keyDao.findById(1L);
        keyService.save(key);
    }

    @Test
    public void generateKeyTest() throws IOException {
        // KeyEntity keyEntity = new KeyEntity();
        // keyEntity.setClientId("clientId");
        // keyEntity.setClientInfo("clientInfo");
        // keyEntity.setClientPassword("clientPassword");
        // ZonedDateTime created = ZonedDateTime.of(2014, 1, 1, 0, 0, 0, 0,
        // ZoneId.of("Z"));
        // ZonedDateTime started = ZonedDateTime.of(2014, 1, 1, 0, 0, 0, 0,
        // ZoneId.of("Z"));
        // ZonedDateTime expired = ZonedDateTime.of(2014, 12, 1, 0, 0, 0, 0,
        // ZoneId.of("Z"));
        // keyEntity.setCreated(created.toInstant().toEpochMilli());
        // keyEntity.setStarted(started.toInstant().toEpochMilli());
        // keyEntity.setExpired(expired.toInstant().toEpochMilli());
        //
        // SettingsUtil settingsUtil = new SettingsUtil();
        // settingsUtil.setMaxDevicesDay(1);
        // settingsUtil.setMaxDevicesWeek(2);
        // settingsUtil.setMaxDevicesMonth(3);
        // settingsUtil.setMaxFormsDay(1);
        // settingsUtil.setMaxFormsWeek(2);
        // settingsUtil.setMaxFormsMonth(3);
        //
        // SettingsCenter settingsCenter = new SettingsCenter();
        // settingsCenter.setBaseCsv(true);
        // settingsCenter.setBaseEthnos(true);
        // settingsCenter.setBaseXls(true);
        // settingsCenter.setBaseXlsx(true);
        // settingsCenter.setExportForms(true);
        // settingsCenter.setImportEthnos(true);
        // settingsCenter.setMaxForms(100);
        // settingsCenter.setMaxResultsDay(1);
        // settingsCenter.setMaxResultsWeek(2);
        // settingsCenter.setMaxResultsMonth(3);
        //
        // SettingsMobile settingsMobile = new SettingsMobile();
        // settingsMobile.setMaxAnswers(100);
        // settingsMobile.setMaxForms(200);
        // settingsMobile.setMaxResultsDay(1);
        // settingsMobile.setMaxResultsWeek(2);
        // settingsMobile.setMaxResultsMonth(3);
        //
        // keyEntity.setSettingsUtil(settingsUtil);
        // keyEntity.setSettingsCenter(settingsCenter);
        // keyEntity.setSettingsMobile(settingsMobile);
        // String key = keyService.generateKey(keyEntity);
        //
        // assertEquals(
        // "+oBHXaxrdQFsaMDmF4za3kDiY2EEBIzpQKY4INVwFvHbPRcoSNTp+UGSNm5g9jH8okjT5XdO/PJv\n"
        // +
        // "2v824Pf+2S+dJeC2TQ8DOBV7lHBlrj6sVBqOsRkIzGTHmtk1vi8TewHxhkORXKnEBGQEUGQBIH8g\n"
        // + "ePhRQ3lPBhfLUP5JTwHZsXJruIw5XPYJVkDNoV1t", key);
    }

    @Test
    @Transactional
    public void testActivateKeyWithClient1() throws IOException {
        prepareSecurity("firstUserName");
        prepareUserAccess(UserAccessCode.CLIENT_ACTIVATE_KEY, true);
        Key key = keyDao.findById(3L);
        keyService.activateAndSaveKey(key);
        key = keyDao.findById(3L);
        assertEquals(key.getStatus(), KeyStatus.ACTIVE);
        assertNotNull(key.getDateBegin());
        assertNotNull(key.getDateEnd());
        assertNotNull(key.getCreatedAt());
        assertTrue(new Date().compareTo(key.getDateBegin()) >= 0);
        assertTrue(key.getDateEnd().compareTo(key.getDateBegin()) > 0);
    }

// TODO: Commented for not valid key implementation.
//    @Test(expected = IllegalStateException.class)
//    @Transactional
//    public void testActivateKeyWithClient2() throws IOException {
//        prepareSecurity("secondUserName");
//        prepareUserAccess(UserAccessCode.CLIENT_ACTIVATE_KEY, true);
//        Key key = keyDao.findById(6L);
//        keyService.activateAndSaveKey(key);
//        key = keyDao.findById(6L);
//        assertEquals(key.getStatus(), KeyStatus.ACTIVE);
//        assertNotNull(key.getDateBegin());
//        assertNotNull(key.getDateEnd());
//        assertNotNull(key.getCreatedAt());
//        assertTrue(new Date().compareTo(key.getDateBegin()) >= 0);
//        assertTrue(key.getDateEnd().compareTo(key.getDateBegin()) > 0);
//        assertEquals(OrderStatus.COMPLETED, key.getOrder().getStatus());
//    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testActivateKeyWithClient3() throws IOException {
        prepareSecurity("firstUserName");
        prepareUserAccess(UserAccessCode.CLIENT_ACTIVATE_KEY, false);
        Key key = keyDao.findById(3L);
        keyService.activateAndSaveKey(key);
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testActivateKeyWithClient4() throws IOException {
        prepareSecurity("firstUserName");
        prepareUserAccess(UserAccessCode.CLIENT_ACTIVATE_KEY, true);
        keyService.activateAndSaveKey(keyDao.findById(4L));
    }

    @Test
    @Transactional
    public void testActivateKeyWithRolePay1() throws IOException {
        prepareRolePaySecurity();
        Key key = keyDao.findById(3L);
        keyService.activateAndSaveKey(key);
        key = keyDao.findById(3L);
        assertEquals(key.getStatus(), KeyStatus.ACTIVE);
        assertNotNull(key.getDateBegin());
        assertNotNull(key.getDateEnd());
        assertNotNull(key.getCreatedAt());
        assertTrue(new Date().compareTo(key.getDateBegin()) >= 0);
        assertTrue(key.getDateEnd().compareTo(key.getDateBegin()) > 0);
    }

    @Test(expected = AccessDeniedException.class)
    @Transactional
    public void testActivateKeyWithAnonymous1() throws IOException {
        prepareAnonymousSecurity();
        Key key = keyDao.findById(3L);
        keyService.activateAndSaveKey(key);
        key = keyDao.findById(3L);
        assertEquals(key.getStatus(), KeyStatus.ACTIVE);
        assertNotNull(key.getDateBegin());
        assertNotNull(key.getDateEnd());
        assertNotNull(key.getCreatedAt());
        assertTrue(new Date().compareTo(key.getDateBegin()) >= 0);
        assertTrue(key.getDateEnd().compareTo(key.getDateBegin()) > 0);
    }

    @Test
    @Transactional
    public void testGetLastActivatedKeyInfo1() {
        List<Key> keys = new ArrayList<Key>();
        Key key = new Key();
        key.setStatus(KeyStatus.CREATED);
        keys.add(key);
        key = new Key();
        key.setStatus(KeyStatus.EXPIRED);
        key.setDateBegin(new Date(0));
        key.setDateEnd(new Date(30L * 24 * 60 * 60 * 1000));
        keys.add(key);
        key = new Key();
        key.setStatus(KeyStatus.ACTIVE);
        key.setDateBegin(new Date(30L * 24 * 60 * 60 * 1000));
        key.setDateEnd(new Date(60L * 24 * 60 * 60 * 1000));
        keys.add(key);
        Object obj = ReflectionTestUtils.invokeMethod(new KeyServiceImpl(),
                "getLastActivatedKeyInfo", (Collection<Key>) keys);
        assertEquals(60L, ReflectionTestUtils.getField(obj, "usedPeriod"));
        assertEquals(key, ReflectionTestUtils.getField(obj, "key"));
    }

    @Test
    @Transactional
    public void testGetLastActivatedKeyInfo2() {
        List<Key> keys = new ArrayList<Key>();
        Key key = new Key();
        key.setStatus(KeyStatus.CREATED);
        keys.add(key);
        key = new Key();
        key.setStatus(KeyStatus.EXPIRED);

        key.setDateBegin(Date.from(ZonedDateTime.of(2014, 1, 1, 0, 0, 0, 0,
                ZoneId.of("Z")).toInstant()));
        key.setDateEnd(Date.from(ZonedDateTime.of(2014, 1, 31, 0, 0, 0, 0,
                ZoneId.of("Z")).toInstant()));
        keys.add(key);
        key = new Key();
        key.setStatus(KeyStatus.ACTIVE);
        key.setDateBegin(Date.from(ZonedDateTime.of(2014, 1, 31, 0, 0, 0, 0,
                ZoneId.of("Z")).toInstant()));
        key.setDateEnd(Date.from(ZonedDateTime.of(2014, 3, 2, 0, 0, 0, 0,
                ZoneId.of("Z")).toInstant()));
        keys.add(key);
        Object obj = ReflectionTestUtils.invokeMethod(new KeyServiceImpl(),
                "getLastActivatedKeyInfo", (Collection<Key>) keys);
        assertEquals(60L, ReflectionTestUtils.getField(obj, "usedPeriod"));
        assertEquals(key, ReflectionTestUtils.getField(obj, "key"));
    }

    @Transactional
    @Test
    public void testListWithClient1() {
        prepareSecurity("firstUserName");
        prepareUserAccess(UserAccessCode.CLIENT_READ_KEY, true);
        KeyListParameters params = new KeyListParameters();
        params.setUserId(((UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal()).getUser().getId());
        keyService.list(params);
    }

    @Transactional
    @Test
    public void testListWithClient2() {
        prepareSecurity("firstUserName");
        prepareUserAccess(UserAccessCode.CLIENT_ACTIVATE_KEY, true);
        KeyListParameters params = new KeyListParameters();
        params.setUserId(((UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal()).getUser().getId());
        keyService.list(params);
    }

    @Transactional
    @Test
    public void testListWithClient3() {
        prepareSecurity("firstUserName");
        prepareUserAccess(UserAccessCode.CLIENT_GET_KEY, true);
        KeyListParameters params = new KeyListParameters();
        params.setUserId(((UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal()).getUser().getId());
        keyService.list(params);
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testListWithClient4() {
        prepareSecurity("firstUserName");
        prepareUserAccess(new UserAccessCode[] {
                UserAccessCode.CLIENT_READ_KEY,
                UserAccessCode.CLIENT_ACTIVATE_KEY,
                UserAccessCode.CLIENT_GET_KEY }, false);
        KeyListParameters params = new KeyListParameters();
        params.setUserId(((UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal()).getUser().getId());
        keyService.list(params);
    }

    @Transactional
    @Test
    public void testCountListWithClient1() {
        prepareSecurity("firstUserName");
        prepareUserAccess(UserAccessCode.CLIENT_READ_KEY, true);
        KeyListParameters params = new KeyListParameters();
        params.setUserId(((UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal()).getUser().getId());
        keyService.countList(params);
    }

    @Transactional
    @Test
    public void testCountListWithClient2() {
        prepareSecurity("firstUserName");
        prepareUserAccess(UserAccessCode.CLIENT_ACTIVATE_KEY, true);
        KeyListParameters params = new KeyListParameters();
        params.setUserId(((UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal()).getUser().getId());
        keyService.countList(params);
    }

    @Transactional
    @Test
    public void testCountListWithClient3() {
        prepareSecurity("firstUserName");
        prepareUserAccess(UserAccessCode.CLIENT_GET_KEY, true);
        KeyListParameters params = new KeyListParameters();
        params.setUserId(((UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal()).getUser().getId());
        keyService.countList(params);
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testCountListWithClient4() {
        prepareSecurity("firstUserName");
        prepareUserAccess(new UserAccessCode[] {
                UserAccessCode.CLIENT_READ_KEY,
                UserAccessCode.CLIENT_ACTIVATE_KEY,
                UserAccessCode.CLIENT_GET_KEY }, false);
        KeyListParameters params = new KeyListParameters();
        params.setUserId(((UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal()).getUser().getId());
        keyService.countList(params);
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testDeleteWithClient1() {
        prepareSecurity("firstUserName");
        prepareUserAccess(UserAccessCode.CLIENT_ACTIVATE_KEY, true);
        keyService.delete(keyDao.findById(1L));
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testDeleteWithClient2() {
        prepareSecurity("firstUserName");
        prepareUserAccess(UserAccessCode.CLIENT_ACTIVATE_KEY, true);
        keyService.delete(keyDao.findById(2L));
    }

    @Transactional
    @Test
    public void testDeleteWithClient3() {
        prepareSecurity("firstUserName");
        prepareUserAccess(UserAccessCode.CLIENT_ACTIVATE_KEY, true);
        keyService.delete(keyDao.findById(3L));
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testDeleteWithClient4() {
        prepareSecurity("firstUserName");
        prepareUserAccess(UserAccessCode.CLIENT_ACTIVATE_KEY, false);
        keyService.delete(keyDao.findById(3L));
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testDeleteWithClient5() {
        prepareSecurity("secondUserName");
        prepareUserAccess(UserAccessCode.CLIENT_ACTIVATE_KEY, true);
        keyService.delete(keyDao.findById(3L));
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testDeleteWithService1() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.CLIENT_ACTIVATE_KEY, true);
        keyService.delete(keyDao.findById(3L));
    }
}
