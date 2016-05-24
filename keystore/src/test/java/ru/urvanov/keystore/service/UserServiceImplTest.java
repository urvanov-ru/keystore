package ru.urvanov.keystore.service;

import static org.junit.Assert.assertNotNull;

import java.io.FileInputStream;
import java.sql.Connection;
import java.util.Date;

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
import ru.urvanov.keystore.dao.UserDao;
import ru.urvanov.keystore.domain.Sex;
import ru.urvanov.keystore.domain.User;
import ru.urvanov.keystore.domain.UserAccessCode;
import ru.urvanov.keystore.domain.UserDetailsImpl;
import ru.urvanov.keystore.domain.UserListParameters;
import ru.urvanov.keystore.exception.UserNameNotUniqueException;
import ru.urvanov.keystore.test.SpringTestBase;

public class UserServiceImplTest extends SpringTestBase {
    @Autowired
    private UserService userService;

    @Autowired
    private UserDao userDao;
    
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
        IDataSet dataSet = new FlatXmlDataSetBuilder().build(new FileInputStream("src/test/java/ru/urvanov/keystore/service/UserServiceImplTest.xml"));

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
        prepareUserAccess(UserAccessCode.CLIENT_ADMIN_CLIENT_ACCOUNT, true);
        assertNotNull(userService.findById(1L));
    }

    @Transactional
    @Test
    public void testFindByIdWithClient2() {
        prepareSecurity("firstUserName");
        prepareUserAccess(UserAccessCode.CLIENT_ADMIN_CLIENT_ACCOUNT, true);
        assertNotNull(userService.findById(4L));
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testFindByIdWithClient3() {
        prepareSecurity("firstUserName");
        prepareUserAccess(UserAccessCode.CLIENT_ADMIN_CLIENT_ACCOUNT, true);
        userService.findById(2L);
    }

    @Transactional
    @Test
    public void testFindByIdWithClient4() {
        prepareSecurity("firstUserName");
        prepareUserAccess(UserAccessCode.CLIENT_ADMIN_CLIENT_ACCOUNT, false);
        userService.findById(1L);
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testFindByIdWithClient5() {
        prepareSecurity("firstUserName");
        prepareUserAccess(UserAccessCode.CLIENT_ADMIN_CLIENT_ACCOUNT, false);
        userService.findById(4L);
    }

    @Transactional
    @Test
    public void testFindByIdWithService1() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_ADMIN_ACCOUNT, true);
        assertNotNull(userService.findById(3L));
    }

    @Transactional
    @Test
    public void testFindByIdWithService2() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_ADMIN_ACCOUNT, true);
        assertNotNull(userService.findById(5L));
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testFindByIdWithService3() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_ADMIN_ACCOUNT, true);
        userService.findById(2L);
    }

    @Transactional
    @Test
    public void testFindByIdWithService4() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_ADMIN_CLIENT, true);
        userService.findById(2L);
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testFindByIdWithService5() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_ADMIN_CLIENT, false);
        userService.findById(2L);
    }

    @Transactional
    @Test
    public void testFindFullByIdWithClient1() {
        prepareSecurity("firstUserName");
        prepareUserAccess(UserAccessCode.CLIENT_ADMIN_CLIENT_ACCOUNT, true);
        assertNotNull(userService.findFullById(1L));
    }

    @Transactional
    @Test
    public void testFindFullByIdWithClient2() {
        prepareSecurity("firstUserName");
        prepareUserAccess(UserAccessCode.CLIENT_ADMIN_CLIENT_ACCOUNT, true);
        assertNotNull(userService.findFullById(4L));
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testFindFullByIdWithClient3() {
        prepareSecurity("firstUserName");
        prepareUserAccess(UserAccessCode.CLIENT_ADMIN_CLIENT_ACCOUNT, true);
        userService.findFullById(2L);
    }

    @Transactional
    @Test
    public void testFindFullByIdWithClient4() {
        prepareSecurity("firstUserName");
        prepareUserAccess(UserAccessCode.CLIENT_ADMIN_CLIENT_ACCOUNT, false);
        userService.findFullById(1L);
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testFindFullByIdWithClient5() {
        prepareSecurity("firstUserName");
        prepareUserAccess(UserAccessCode.CLIENT_ADMIN_CLIENT_ACCOUNT, false);
        userService.findFullById(4L);
    }

    @Transactional
    @Test
    public void testFindFullByIdWithService1() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_ADMIN_ACCOUNT, true);
        assertNotNull(userService.findFullById(3L));
    }

    @Transactional
    @Test
    public void testFindFullByIdWithService2() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_ADMIN_ACCOUNT, true);
        assertNotNull(userService.findFullById(5L));
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testFindFullByIdWithService3() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_ADMIN_ACCOUNT, true);
        userService.findFullById(2L);
    }

    @Transactional
    @Test
    public void testFindFullByIdWithService4() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_ADMIN_CLIENT, true);
        userService.findFullById(2L);
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testFindFullByIdWithService5() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_ADMIN_CLIENT, false);
        userService.findFullById(2L);
    }

    @Transactional
    @Test
    public void testGetReferenceWithClient1() {
        prepareSecurity("firstUserName");
        prepareUserAccess(UserAccessCode.CLIENT_ADMIN_CLIENT_ACCOUNT, true);
        assertNotNull(userService.getReference(1L));
    }

    @Transactional
    @Test
    public void testGetReferenceWithClient2() {
        prepareSecurity("firstUserName");
        prepareUserAccess(UserAccessCode.CLIENT_ADMIN_CLIENT_ACCOUNT, true);
        assertNotNull(userService.getReference(4L));
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testGetReferenceWithClient3() {
        prepareSecurity("firstUserName");
        prepareUserAccess(UserAccessCode.CLIENT_ADMIN_CLIENT_ACCOUNT, true);
        userService.getReference(2L);
    }

    @Transactional
    @Test
    public void testGetReferenceWithClient4() {
        prepareSecurity("firstUserName");
        prepareUserAccess(UserAccessCode.CLIENT_ADMIN_CLIENT_ACCOUNT, false);
        userService.getReference(1L);
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testGetReferenceWithClient5() {
        prepareSecurity("firstUserName");
        prepareUserAccess(UserAccessCode.CLIENT_ADMIN_CLIENT_ACCOUNT, false);
        userService.getReference(5L);
    }

    @Transactional
    @Test
    public void testGetReferenceWithService1() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_ADMIN_ACCOUNT, true);
        assertNotNull(userService.getReference(3L));
    }

    @Transactional
    @Test
    public void testGetReferenceWithService2() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_ADMIN_ACCOUNT, true);
        assertNotNull(userService.getReference(5L));
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testGetReferenceWithService3() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_ADMIN_ACCOUNT, true);
        userService.getReference(2L);
    }

    @Transactional
    @Test
    public void testGetReferenceWithService4() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_ADMIN_CLIENT, true);
        userService.getReference(2L);
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testGetReferenceWithService5() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_ADMIN_CLIENT, false);
        userService.getReference(2L);
    }

    @Transactional
    @Test
    public void testSaveWithClient1() throws UserNameNotUniqueException {
        prepareSecurity("firstUserName");
        prepareUserAccess(UserAccessCode.CLIENT_ADMIN_CLIENT_ACCOUNT, true);
        userService.save(userDao.findFullById(1L));
    }

    @Transactional
    @Test
    public void testSaveWithClient2() throws UserNameNotUniqueException {
        prepareSecurity("firstUserName");
        prepareUserAccess(UserAccessCode.CLIENT_ADMIN_CLIENT_ACCOUNT, true);
        userService.save(userDao.findById(4L));
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testSaveWithClient3() throws UserNameNotUniqueException {
        prepareSecurity("firstUserName");
        prepareUserAccess(UserAccessCode.CLIENT_ADMIN_CLIENT_ACCOUNT, true);
        userService.save(userDao.findById(2L));
    }

    @Transactional
    @Test
    public void testSaveWithClient4() throws UserNameNotUniqueException {
        prepareSecurity("firstUserName");
        prepareUserAccess(UserAccessCode.CLIENT_ADMIN_CLIENT_ACCOUNT, false);
        userService.save(userDao.findFullById(1L));
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testSaveWithClient5() throws UserNameNotUniqueException {
        prepareSecurity("firstUserName");
        prepareUserAccess(UserAccessCode.CLIENT_ADMIN_CLIENT_ACCOUNT, false);
        userService.save(userDao.findById(4L));
    }

    @Transactional
    @Test
    public void testSaveWithService1() throws UserNameNotUniqueException {
        prepareSecurity("service@nowhere.com");
        userService.save(userDao.findById(3L));
    }

    @Transactional
    @Test
    public void testSaveWithService2() throws UserNameNotUniqueException {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_ADMIN_ACCOUNT, true);
        userService.save(userDao.findById(5L));
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testSaveWithService3() throws UserNameNotUniqueException {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_ADMIN_ACCOUNT, false);
        userService.save(userDao.findById(5L));
    }

    @Transactional
    @Test
    public void testSaveWithService4() throws UserNameNotUniqueException {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_ADMIN_CLIENT, true);
        userService.save(userDao.findById(4L));
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testSaveWithService5() throws UserNameNotUniqueException {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_ADMIN_CLIENT, false);
        userService.save(userDao.findById(4L));
    }

    @Transactional
    @Test
    public void testSaveAnonymous1() throws UserNameNotUniqueException {
        prepareAnonymousSecurity();
        User user = new User();
        user.setActivated(true);
        user.setBirthdate(new Date());
        user.setClient(clientDao.findById(1L));
        user.setUserName("blablablafasdg@asdlkgh.com");
        user.setFullName("ajskldghasjdhguiwebi hweui");
        user.setPassword("asdgawegasedfasdgasdfsf");
        user.setPhone("phone");
        user.setPost("post");
        user.setSex(Sex.MAN);
        user.setEnabled(true);
        userService.save(user);
    }
    
    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testBlockWithClient1() throws UserNameNotUniqueException {
        prepareSecurity("firstUserName");
        prepareUserAccess(UserAccessCode.CLIENT_ADMIN_CLIENT_ACCOUNT, true);
        userService.block(userDao.findFullById(1L));
    }

    @Transactional
    @Test
    public void testBlockWithClient2() throws UserNameNotUniqueException {
        prepareSecurity("firstUserName");
        prepareUserAccess(UserAccessCode.CLIENT_ADMIN_CLIENT_ACCOUNT, true);
        userService.block(userDao.findById(4L));
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testBlockWithClient3() throws UserNameNotUniqueException {
        prepareSecurity("firstUserName");
        prepareUserAccess(UserAccessCode.CLIENT_ADMIN_CLIENT_ACCOUNT, true);
        userService.block(userDao.findById(2L));
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testBlockWithClient4() throws UserNameNotUniqueException {
        prepareSecurity("firstUserName");
        prepareUserAccess(UserAccessCode.CLIENT_ADMIN_CLIENT_ACCOUNT, false);
        userService.block(userDao.findById(4L));
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testBlockWithService1() throws UserNameNotUniqueException {
        prepareSecurity("service@nowhere.com");
        userService.block(userDao.findById(3L));
    }

    @Transactional
    @Test
    public void testBlockWithService2() throws UserNameNotUniqueException {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_ADMIN_ACCOUNT, true);
        userService.block(userDao.findById(5L));
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testBlockWithService3() throws UserNameNotUniqueException {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_ADMIN_ACCOUNT, false);
        userService.block(userDao.findById(5L));
    }

    @Transactional
    @Test
    public void testBlockWithService4() throws UserNameNotUniqueException {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_ADMIN_CLIENT, true);
        userService.block(userDao.findById(4L));
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testBlockWithService5() throws UserNameNotUniqueException {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_ADMIN_CLIENT, false);
        userService.block(userDao.findById(4L));
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testGetUserAccessesWithClient1()
            throws UserNameNotUniqueException {
        prepareSecurity("firstUserName");
        prepareUserAccess(UserAccessCode.CLIENT_ADMIN_CLIENT_ACCOUNT, true);
        assertNotNull(userService.getUserAccesses(userDao.findFullById(1L)));
    }

    @Transactional
    @Test
    public void testGetUserAccessesWithClient2()
            throws UserNameNotUniqueException {
        prepareSecurity("firstUserName");
        prepareUserAccess(UserAccessCode.CLIENT_ADMIN_CLIENT_ACCOUNT, true);
        assertNotNull(userService.getUserAccesses(userDao.findById(4L)));
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testGetUserAccessesWithClient3()
            throws UserNameNotUniqueException {
        prepareSecurity("firstUserName");
        prepareUserAccess(UserAccessCode.CLIENT_ADMIN_CLIENT_ACCOUNT, true);
        userService.getUserAccesses(userDao.findById(2L));
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testGetUserAccessesWithClient4()
            throws UserNameNotUniqueException {
        prepareSecurity("firstUserName");
        prepareUserAccess(UserAccessCode.CLIENT_ADMIN_CLIENT_ACCOUNT, false);
        assertNotNull(userService.getUserAccesses(userDao.findFullById(4L)));
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testGetUserAccessesWithService1()
            throws UserNameNotUniqueException {
        prepareSecurity("service@nowhere.com");
        userService.getUserAccesses(userDao.findById(3L));
    }

    @Transactional
    @Test
    public void testGetUserAccessesWithService2()
            throws UserNameNotUniqueException {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_ADMIN_ACCOUNT, true);
        userService.getUserAccesses(userDao.findById(5L));
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testGetUserAccessesWithService3()
            throws UserNameNotUniqueException {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_ADMIN_ACCOUNT, false);
        userService.getUserAccesses(userDao.findById(5L));
    }

    @Transactional
    @Test
    public void testGetUserAccessesWithService4()
            throws UserNameNotUniqueException {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_ADMIN_CLIENT, true);
        userService.getUserAccesses(userDao.findById(4L));
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testGetUserAccessesWithService5()
            throws UserNameNotUniqueException {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_ADMIN_CLIENT, false);
        userService.getUserAccesses(userDao.findById(4L));
    }

    @Transactional
    @Test
    public void testListWithClient1() {
        prepareSecurity("firstUserName");
        prepareUserAccess(UserAccessCode.CLIENT_ADMIN_CLIENT_ACCOUNT, true);
        UserListParameters params = new UserListParameters();
        params.setUserId(((UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal()).getUser().getId());
        userService.list(params);
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testListWithClient2() {
        prepareSecurity("firstUserName");
        prepareUserAccess(UserAccessCode.CLIENT_ADMIN_CLIENT_ACCOUNT, false);
        UserListParameters params = new UserListParameters();
        params.setUserId(((UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal()).getUser().getId());
        userService.list(params);
    }

    @Transactional
    @Test
    public void testListWithService1() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_ADMIN_ACCOUNT, true);
        UserListParameters params = new UserListParameters();
        params.setUserId(((UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal()).getUser().getId());
        userService.list(params);
    }

    @Transactional
    @Test
    public void testListWithService2() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_ADMIN_CLIENT, true);
        UserListParameters params = new UserListParameters();
        params.setUserId(((UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal()).getUser().getId());
        userService.list(params);
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testListWithService3() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(new UserAccessCode[] {
                UserAccessCode.SERVICE_ADMIN_ACCOUNT,
                UserAccessCode.SERVICE_ADMIN_CLIENT }, false);
        UserListParameters params = new UserListParameters();
        params.setUserId(((UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal()).getUser().getId());
        userService.list(params);
    }

    @Transactional
    @Test
    public void testCountListWithClient1() {
        prepareSecurity("firstUserName");
        prepareUserAccess(UserAccessCode.CLIENT_ADMIN_CLIENT_ACCOUNT, true);
        UserListParameters params = new UserListParameters();
        params.setUserId(((UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal()).getUser().getId());
        userService.countList(params);
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testCountListWithClient2() {
        prepareSecurity("firstUserName");
        prepareUserAccess(UserAccessCode.CLIENT_ADMIN_CLIENT_ACCOUNT, false);
        UserListParameters params = new UserListParameters();
        params.setUserId(((UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal()).getUser().getId());
        userService.countList(params);
    }

    @Transactional
    @Test
    public void testCountListWithService1() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_ADMIN_ACCOUNT, true);
        UserListParameters params = new UserListParameters();
        params.setUserId(((UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal()).getUser().getId());
        userService.countList(params);
    }

    @Transactional
    @Test
    public void testCountListWithService2() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_ADMIN_CLIENT, true);
        UserListParameters params = new UserListParameters();
        params.setUserId(((UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal()).getUser().getId());
        userService.countList(params);
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testCountListWithService3() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(new UserAccessCode[] {
                UserAccessCode.SERVICE_ADMIN_ACCOUNT,
                UserAccessCode.SERVICE_ADMIN_CLIENT }, false);
        UserListParameters params = new UserListParameters();
        params.setUserId(((UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal()).getUser().getId());
        userService.countList(params);
    }
}
