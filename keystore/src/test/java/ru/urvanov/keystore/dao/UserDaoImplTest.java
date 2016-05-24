package ru.urvanov.keystore.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.FileInputStream;
import java.math.BigInteger;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

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

import ru.urvanov.keystore.domain.Authority;
import ru.urvanov.keystore.domain.DictEvent;
import ru.urvanov.keystore.domain.DictEventCode;
import ru.urvanov.keystore.domain.LinkUserDictEventNotification;
import ru.urvanov.keystore.domain.User;
import ru.urvanov.keystore.domain.UserAccessCode;
import ru.urvanov.keystore.domain.UserListParameters;
import ru.urvanov.keystore.test.SpringTestBase;

public class UserDaoImplTest extends SpringTestBase {
    @Autowired
    private UserDao userDao;

    @Autowired
    private DictEventDao dictEventDao;

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
        IDataSet dataSet = new FlatXmlDataSetBuilder().build(new FileInputStream("src/test/java/ru/urvanov/keystore/dao/UserDaoImplTest.xml"));

        try (Connection connection = dataSource.getConnection())
        {
            databaseConnection = new DatabaseConnection(connection);
            DatabaseOperation.CLEAN_INSERT.execute(databaseConnection, dataSet);
        }
    }
    
    @Test
    @Transactional
    public void testFind1() {
        User user = userDao.findById(1L);
        assertNotNull(user);
        assertEquals("firstUserName", user.getUserName());
    }

    @Test
    @Transactional
    public void testFind2() {
        User user = userDao.findByUserName("firstUserName");
        assertNotNull(user);
        assertEquals("firstUserName", user.getUserName());
    }

    @Test
    @Transactional
    public void testFindFullByUserName1() {
        User user = userDao.findFullByUserName("firstUserName");
        assertNotNull(user);
        assertEquals("firstUserName", user.getUserName());
        assertEquals(2, user.getAuthorities().size());
    }

    @Test
    @Transactional
    public void testFindFullByUserName2() {
        User user = userDao.findFullByUserName("asdgfsdag sdfg sdfg");
        assertNull(user);
    }

    @Test
    @Transactional
    public void testSave() {
        User user = new User();
        user.setUserName("newUserName");
        user.setPassword("newPassword");
        user.setEnabled(true);
        user.setClient(clientDao.getReference(1L));
        userDao.save(user);
        Long id = user.getId();
        assertNotNull(id);
        user = userDao.findById(id);
        assertNotNull(user);
        assertNotNull(user.getAuthorities());
        assertEquals("newUserName", user.getUserName());
        assertEquals("newPassword", user.getPassword());
    }

    @Test
    @Transactional
    public void testFindFullById() {
        User user = userDao.findFullById(1L);
        assertNotNull(user);
        assertEquals(new Long(1L), user.getId());
        assertEquals(2, user.getUserAccesses().size());
        assertEquals(2, user.getAuthorities().size());
        assertEquals(true,
                user.getUserAccesses().get(UserAccessCode.SERVICE_READ_ORDER)
                        .getAccess());
        assertEquals(false,
                user.getUserAccesses().get(UserAccessCode.SERVICE_READ_PAYMENT)
                        .getAccess());
        assertEquals(1, user.getLinkUserDictEventNotifications().size());
        assertEquals(
                true,
                user.getLinkUserDictEventNotifications()
                        .get(dictEventDao
                                .findByCode(DictEventCode.CLIENT_ADDED))
                        .getAllowNotification());
    }

    @Test
    @Transactional
    public void testFindAuthorities() {
        User user = userDao.findFullById(1L);
        assertEquals(2, user.getAuthorities().size());
    }

    @Test
    @Transactional
    public void testSaveAuthority() {
        User user = userDao.findFullById(1L);
        assertEquals(2, user.getAuthorities().size());
        Authority authority = new Authority();
        authority.setAuthority("ROLE_TEMP");
        authority.setUser(user);
        user.getAuthorities().add(authority);
        userDao.save(user);
        user = userDao.findFullById(1L);
        assertEquals(3, user.getAuthorities().size());
    }

    @Test
    @Transactional
    public void testSaveLinkUserDictEventNotification1() {
        User user = userDao.findFullById(1L);
        Map<DictEvent, LinkUserDictEventNotification> map = user
                .getLinkUserDictEventNotifications();
        DictEvent dictEvent = dictEventDao.getReference(2L);
        LinkUserDictEventNotification luden = new LinkUserDictEventNotification();
        luden.setAllowNotification(true);
        luden.setDictEvent(dictEvent);
        luden.setUser(user);
        map.put(dictEvent, luden);
        userDao.save(user);
        user = userDao.findFullById(1L);
        map = user.getLinkUserDictEventNotifications();
        luden = map.get(dictEventDao.findById(1L));
        assertEquals(dictEventDao.findById(1L), luden.getDictEvent());
        assertEquals(true, luden.getAllowNotification());
    }

    @Test
    @Transactional
    public void testCheckUserNameUnique() {
        User user = new User();
        user.setUserName("firstUserName");
        assertEquals(false, userDao.checkUserNameUnique(user));
    }

    @Test
    @Transactional
    public void testCheckUserNameUnique2() {
        User user = new User();
        user.setUserName("asdjhgasdg");
        assertEquals(true, userDao.checkUserNameUnique(user));
    }

    @Test
    @Transactional
    public void testCheckUserNameUnique3() {
        User user = userDao.findFullByUserName("firstUserName");
        assertEquals(true, userDao.checkUserNameUnique(user));
    }

    @Test
    @Transactional
    public void testList1() {
        UserListParameters params = new UserListParameters();
        params.setUserId(1L);
        assertEquals(2, userDao.list(params).size());
    }

    @Test
    @Transactional
    public void testCountList1() {
        UserListParameters params = new UserListParameters();
        params.setUserId(1L);
        assertEquals(BigInteger.valueOf(2), userDao.countList(params));
    }

    @Test
    @Transactional
    public void testFindEnabledByClientId() {
        assertEquals(2, userDao.findEnabledByClientId(1L).size());
    }
    
    @Test
    @Transactional
    public void testFindForNewClientNotification() {
        List<User> lst = userDao.findForNewClientNotification();
        assertEquals(1, lst.size());
    }
    
    @Test
    @Transactional
    public void testFindByTodayBirthday() {
        List<User> lst  = userDao.findByTodayBirthday();
        assertNotNull(lst);
    }
}
