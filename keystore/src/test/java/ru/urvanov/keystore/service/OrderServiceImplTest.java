package ru.urvanov.keystore.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.FileInputStream;
import java.math.BigDecimal;
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

import ru.urvanov.keystore.dao.DictServiceTypeDao;
import ru.urvanov.keystore.dao.OrderDao;
import ru.urvanov.keystore.dao.UserDao;
import ru.urvanov.keystore.domain.KeyActivationMode;
import ru.urvanov.keystore.domain.Order;
import ru.urvanov.keystore.domain.OrderListParameters;
import ru.urvanov.keystore.domain.OrderStatus;
import ru.urvanov.keystore.domain.UserAccessCode;
import ru.urvanov.keystore.domain.UserDetailsImpl;
import ru.urvanov.keystore.test.SpringTestBase;

public class OrderServiceImplTest extends SpringTestBase {
    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private DictServiceTypeDao dictServiceTypeDao;

    @Autowired
    private DataSource dataSource;
    
    @Before
    public void before() throws Exception {

        // initialize your database connection here
        IDatabaseConnection databaseConnection = null;
        // ...

        // initialize your dataset here
        IDataSet dataSet = new FlatXmlDataSetBuilder().build(new FileInputStream("src/test/java/ru/urvanov/keystore/service/OrderServiceImplTest.xml"));

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
        prepareUserAccess(UserAccessCode.CLIENT_READ_ORDER, true);
        assertNotNull(orderService.findById(1L));
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testFindByIdWithClient2() {
        prepareSecurity("firstUserName");
        prepareUserAccess(UserAccessCode.CLIENT_READ_ORDER, true);
        assertNotNull(orderService.findById(3L));
    }

    @Transactional
    @Test
    public void testFindByIdWithClient3() {
        prepareSecurity("firstUserName");
        prepareUserAccess(UserAccessCode.CLIENT_ADD_ORDER, true);
        assertNotNull(orderService.findById(1L));
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testFindByIdWithClient4() {
        prepareSecurity("firstUserName");
        prepareUserAccess(UserAccessCode.CLIENT_ADD_ORDER, true);
        assertNotNull(orderService.findById(3L));
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testFindByIdWithClient5() {
        prepareSecurity("firstUserName");
        prepareUserAccess(new UserAccessCode[] {
                UserAccessCode.CLIENT_READ_ORDER,
                UserAccessCode.CLIENT_ADD_ORDER }, true);
        assertNotNull(orderService.findById(3L));
    }

    @Transactional
    @Test
    public void testFindByIdWithService1() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_READ_ORDER, true);
        assertNotNull(orderService.findById(1L));
    }

    @Transactional
    @Test
    public void testFindByIdWithService2() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_EXPORT_ORDER, true);
        assertNotNull(orderService.findById(1L));
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testFindByIdWithService3() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(new UserAccessCode[] {
                UserAccessCode.SERVICE_READ_ORDER,
                UserAccessCode.SERVICE_EXPORT_ORDER }, false);
        assertNotNull(orderService.findById(1L));
    }

    @Transactional
    @Test
    public void testFindFullByIdWithClient1() {
        prepareSecurity("firstUserName");
        prepareUserAccess(UserAccessCode.CLIENT_READ_ORDER, true);
        assertNotNull(orderService.findFullById(1L));
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testFindFullByIdWithClient2() {
        prepareSecurity("firstUserName");
        prepareUserAccess(UserAccessCode.CLIENT_READ_ORDER, true);
        assertNotNull(orderService.findFullById(3L));
    }

    @Transactional
    @Test
    public void testFindFullByIdWithClient3() {
        prepareSecurity("firstUserName");
        prepareUserAccess(UserAccessCode.CLIENT_ADD_ORDER, true);
        assertNotNull(orderService.findFullById(1L));
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testFindFullByIdWithClient4() {
        prepareSecurity("firstUserName");
        prepareUserAccess(UserAccessCode.CLIENT_ADD_ORDER, true);
        assertNotNull(orderService.findFullById(3L));
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testFindFullByIdWithClient5() {
        prepareSecurity("firstUserName");
        prepareUserAccess(new UserAccessCode[] {
                UserAccessCode.CLIENT_READ_ORDER,
                UserAccessCode.CLIENT_ADD_ORDER }, true);
        assertNotNull(orderService.findFullById(3L));
    }

    @Transactional
    @Test
    public void testFindFullByIdWithService1() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_READ_ORDER, true);
        assertNotNull(orderService.findFullById(1L));
    }

    @Transactional
    @Test
    public void testFindFullByIdWithService2() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_EXPORT_ORDER, true);
        assertNotNull(orderService.findFullById(1L));
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testFindFullByIdWithService3() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(new UserAccessCode[] {
                UserAccessCode.SERVICE_READ_ORDER,
                UserAccessCode.SERVICE_EXPORT_ORDER }, false);
        assertNotNull(orderService.findFullById(1L));
    }

    @Transactional
    @Test
    public void testSaveWithClient1() {
        prepareSecurity("firstUserName");
        prepareUserAccess(UserAccessCode.CLIENT_ADD_ORDER, true);
        Order order = new Order();
        order.setCreatedAt(new Date());
        order.setCreatedBy(userDao
                .findById(((UserDetailsImpl) SecurityContextHolder.getContext()
                        .getAuthentication().getPrincipal()).getUser().getId()));
        order.setDictServiceType(dictServiceTypeDao.findById(1L));
        order.setKeyActivationMode(KeyActivationMode.AUTOMATIC);
        order.setStatus(OrderStatus.PENDING_PAYMENT);
        orderService.save(order);
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testSaveWithClient2() {
        prepareSecurity("firstUserName");
        prepareUserAccess(UserAccessCode.CLIENT_ADD_ORDER, false);
        Order order = new Order();
        order.setCreatedAt(new Date());
        order.setCreatedBy(userDao
                .findById(((UserDetailsImpl) SecurityContextHolder.getContext()
                        .getAuthentication().getPrincipal()).getUser().getId()));
        order.setDictServiceType(dictServiceTypeDao.findById(1L));
        order.setKeyActivationMode(KeyActivationMode.AUTOMATIC);
        order.setStatus(OrderStatus.PENDING_PAYMENT);
        orderService.save(order);
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testSaveWithClient3() {
        prepareSecurity("firstUserName");
        prepareUserAccess(UserAccessCode.CLIENT_ADD_ORDER, false);
        orderService.save(orderDao.findById(3L));
    }

    @Transactional
    @Test
    public void testListWithService1() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_READ_ORDER, true);
        OrderListParameters params = new OrderListParameters();
        params.setUserId(((UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal()).getUser().getId());
        assertNotNull(orderService.list(params));
    }

    @Transactional
    @Test
    public void testListWithService2() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_EXPORT_ORDER, true);
        OrderListParameters params = new OrderListParameters();
        params.setUserId(((UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal()).getUser().getId());
        assertNotNull(orderService.list(params));
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testListWithService3() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(new UserAccessCode[] {
                UserAccessCode.SERVICE_READ_ORDER,
                UserAccessCode.SERVICE_EXPORT_ORDER }, false);
        OrderListParameters params = new OrderListParameters();
        params.setUserId(((UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal()).getUser().getId());
        assertNotNull(orderService.list(params));
    }

    @Transactional
    @Test
    public void testCountListWithService1() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_READ_ORDER, true);
        OrderListParameters params = new OrderListParameters();
        params.setUserId(((UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal()).getUser().getId());
        assertNotNull(orderService.countList(params));
    }

    @Transactional
    @Test
    public void testCountListWithService2() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_EXPORT_ORDER, true);
        OrderListParameters params = new OrderListParameters();
        params.setUserId(((UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal()).getUser().getId());
        assertNotNull(orderService.countList(params));
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testCountListWithService3() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(new UserAccessCode[] {
                UserAccessCode.SERVICE_READ_ORDER,
                UserAccessCode.SERVICE_EXPORT_ORDER }, false);
        OrderListParameters params = new OrderListParameters();
        params.setUserId(((UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal()).getUser().getId());
        assertNotNull(orderService.countList(params));
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testSaveWithService1() {
        prepareSecurity("service@nowhere.com");
        orderService.save(orderDao.findById(1L));
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testSaveWithService2() {
        prepareSecurity("service@nowhere.com");
        Order order = new Order();
        order.setCreatedAt(new Date());
        order.setCreatedBy(((UserDetailsImpl) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal()).getUser());
        order.setDictServiceType(dictServiceTypeDao.findById(1L));
        order.setKeyActivationMode(KeyActivationMode.AUTOMATIC);
        orderService.save(order);
    }

    @Transactional
    @Test
    public void testListWithClient1() {
        prepareSecurity("firstUserName");
        prepareUserAccess(UserAccessCode.CLIENT_READ_ORDER, true);
        OrderListParameters params = new OrderListParameters();
        params.setUserId(((UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal()).getUser().getId());
        assertNotNull(orderService.list(params));
    }

    @Transactional
    @Test
    public void testListWithClient2() {
        prepareSecurity("firstUserName");
        prepareUserAccess(UserAccessCode.CLIENT_ADD_ORDER, true);
        OrderListParameters params = new OrderListParameters();
        params.setUserId(((UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal()).getUser().getId());
        assertNotNull(orderService.list(params));
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testListWithClient3() {
        prepareSecurity("firstUserName");
        prepareUserAccess(new UserAccessCode[] {
                UserAccessCode.CLIENT_READ_ORDER,
                UserAccessCode.CLIENT_ADD_ORDER }, false);
        OrderListParameters params = new OrderListParameters();
        params.setUserId(((UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal()).getUser().getId());
        assertNotNull(orderService.list(params));
    }

    @Transactional
    @Test
    public void testCountListWithClient1() {
        prepareSecurity("firstUserName");
        prepareUserAccess(UserAccessCode.CLIENT_READ_ORDER, true);
        OrderListParameters params = new OrderListParameters();
        params.setUserId(((UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal()).getUser().getId());
        assertNotNull(orderService.countList(params));
    }

    @Transactional
    @Test
    public void testCountListWithClient2() {
        prepareSecurity("firstUserName");
        prepareUserAccess(UserAccessCode.CLIENT_ADD_ORDER, true);
        OrderListParameters params = new OrderListParameters();
        params.setUserId(((UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal()).getUser().getId());
        assertNotNull(orderService.countList(params));
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testCountListWithClient3() {
        prepareSecurity("firstUserName");
        prepareUserAccess(new UserAccessCode[] {
                UserAccessCode.CLIENT_READ_ORDER,
                UserAccessCode.CLIENT_ADD_ORDER }, false);
        OrderListParameters params = new OrderListParameters();
        params.setUserId(((UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal()).getUser().getId());
        assertNotNull(orderService.countList(params));
    }
    
    
    @Transactional
    @Test
    public void testCalculateRemainderWithClient1() {
        prepareSecurity("firstUserName");
        prepareUserAccess(UserAccessCode.CLIENT_READ_ORDER, true);
        assertNotNull(orderService.calculateRemainder(orderDao.findById(1L)));
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testCalculateRemainderWithClient2() {
        prepareSecurity("firstUserName");
        prepareUserAccess(UserAccessCode.CLIENT_READ_ORDER, true);
        assertNotNull(orderService.calculateRemainder(orderDao.findById(3L)));
    }

    @Transactional
    @Test
    public void testCalculateRemainderWithClient3() {
        prepareSecurity("firstUserName");
        prepareUserAccess(UserAccessCode.CLIENT_ADD_ORDER, true);
        assertNotNull(orderService.calculateRemainder(orderDao.findById(1L)));
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void  testCalculateRemainderWithClient4() {
        prepareSecurity("firstUserName");
        prepareUserAccess(UserAccessCode.CLIENT_ADD_ORDER, true);
        assertNotNull(orderService.calculateRemainder(orderDao.findById(3L)));
    }

    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void testCalculateRemainderWithClient5() {
        prepareSecurity("firstUserName");
        prepareUserAccess(new UserAccessCode[] {
                UserAccessCode.CLIENT_READ_ORDER,
                UserAccessCode.CLIENT_ADD_ORDER }, true);
        assertNotNull(orderService.calculateRemainder(orderDao.findById(3L)));
    }
    
    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void cancelOrderWithClient1() {
        prepareSecurity("firstUserName");
        prepareUserAccess(new UserAccessCode[]{
                UserAccessCode.CLIENT_ADD_ORDER
        }, true);
        orderService.cancelOrder(orderDao.findById(4L));
    }
    
    @Transactional
    @Test
    public void cancelOrderWithClient2() {
        prepareSecurity("secondUserName");
        prepareUserAccess(new UserAccessCode[]{
                UserAccessCode.CLIENT_ADD_ORDER
        }, true);
        orderService.cancelOrder(orderDao.findById(4L));
    }
    
    @Transactional
    @Test(expected = IllegalStateException.class)
    public void cancelOrderWithClient3() {
        prepareSecurity("secondUserName");
        prepareUserAccess(new UserAccessCode[]{
                UserAccessCode.CLIENT_ADD_ORDER
        }, true);
        orderService.cancelOrder(orderDao.findById(1L));
    }
    
    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void cancelOrderWithClient4() {
        prepareSecurity("secondUserName");
        prepareUserAccess(new UserAccessCode[]{
                UserAccessCode.CLIENT_READ_ORDER
        }, true);
        orderService.cancelOrder(orderDao.findById(4L));
    }
    
    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void cancelOrderWithService1() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(new UserAccessCode[]{
                UserAccessCode.CLIENT_ADD_ORDER
        }, true);
        orderService.cancelOrder(orderDao.findById(4L));
    }
    
    @Transactional
    @Test
    public void testCheckAmountById1() {
        prepareAnonymousSecurity();
        assertTrue(orderService.checkPayAmountById(4L, new BigDecimal("1234.0")));
    }
    
    
    @Transactional
    @Test
    public void testCheckAmountById2() {
        prepareAnonymousSecurity();
        assertFalse(orderService.checkPayAmountById(4L, new BigDecimal("2222")));
    }
    
    @Transactional
    @Test
    public void testCheckAmountById3() {
        prepareAnonymousSecurity();
        assertFalse(orderService.checkPayAmountById(1L, new BigDecimal("121.01")));
    }
    
    
    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void payBackOrderWithClient1() {
        prepareSecurity("firstUserName");
        prepareUserAccess(new UserAccessCode[]{
                UserAccessCode.CLIENT_ADD_ORDER
        }, true);
        orderService.payBackOrder(orderDao.findById(6L));
    }
    
    @Transactional
    @Test
    public void payBackOrderWithClient2() {
        prepareSecurity("secondUserName");
        prepareUserAccess(new UserAccessCode[]{
                UserAccessCode.CLIENT_ADD_ORDER
        }, true);
        orderService.payBackOrder(orderDao.findById(6L));
    }
    
    @Transactional
    @Test(expected = IllegalStateException.class)
    public void payBackOrderWithClient3() {
        prepareSecurity("secondUserName");
        prepareUserAccess(new UserAccessCode[]{
                UserAccessCode.CLIENT_ADD_ORDER
        }, true);
        orderService.payBackOrder(orderDao.findById(4L));
    }
    
    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void payBackOrderWithClient4() {
        prepareSecurity("secondUserName");
        prepareUserAccess(new UserAccessCode[]{
                UserAccessCode.CLIENT_READ_ORDER
        }, true);
        orderService.payBackOrder(orderDao.findById(6L));
    }
    
    @Transactional
    @Test(expected = AccessDeniedException.class)
    public void payBackOrderWithService1() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(new UserAccessCode[]{
                UserAccessCode.CLIENT_ADD_ORDER
        }, true);
        orderService.payBackOrder(orderDao.findById(6L));
    }
}
