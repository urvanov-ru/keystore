package ru.urvanov.keystore.service;

import static org.junit.Assert.assertEquals;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;

import javax.sql.DataSource;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;

import ru.urvanov.keystore.dao.OrderDao;
import ru.urvanov.keystore.domain.Order;
import ru.urvanov.keystore.domain.OrderStatus;
import ru.urvanov.keystore.domain.PaymentListParameters;
import ru.urvanov.keystore.domain.PaymentType;
import ru.urvanov.keystore.domain.UserAccessCode;
import ru.urvanov.keystore.domain.UserDetailsImpl;
import ru.urvanov.keystore.test.SpringTestBase;

public class PaymentServiceImplTest extends SpringTestBase {
    @Autowired
    private PaymentService paymentService;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private OrderDao orderDao;

    @Before
    public void before() throws Exception {

        // initialize your database connection here
        IDatabaseConnection databaseConnection = null;
        // ...

        // initialize your dataset here
        IDataSet dataSet = new FlatXmlDataSetBuilder()
                .build(new FileInputStream(
                        "src/test/java/ru/urvanov/keystore/service/PaymentServiceImplTest.xml"));

        try (Connection connection = dataSource.getConnection()) {
            databaseConnection = new DatabaseConnection(connection);
            DatabaseOperation.CLEAN_INSERT.execute(databaseConnection, dataSet);
        }
    }

    @Test(expected = AccessDeniedException.class)
    public void testCreatePaymentForOrder1() throws IOException {
        this.prepareAnonymousSecurity();
        paymentService.createPaymentForOrder(4L, PaymentType.ORDER_PAYMENT,
                new BigDecimal("1234.00"), new BigDecimal("10.01"), null);
        Order order = orderDao.findFullById(4L);
        assertEquals(1, order.getPayments().size());
        assertEquals(OrderStatus.PAYED, order.getStatus());
    }

    @Test
    public void testCreatePaymentForOrder2() throws IOException {
        this.prepareRolePaySecurity();
        paymentService.createPaymentForOrder(4L, PaymentType.ORDER_PAYMENT,
                new BigDecimal("1234.00"), new BigDecimal("10.01"), null);
        Order order = orderDao.findFullById(4L);
        assertEquals(1, order.getPayments().size());
        assertEquals(OrderStatus.PAYED, order.getStatus());
    }

// TODO: Commented because of not valid key implementation.
//    @Test
//    public void testCreatePaymentForOrder3() throws IOException {
//        this.prepareRolePaySecurity();
//        paymentService.createPaymentForOrder(5L, PaymentType.ORDER_PAYMENT,
//                new BigDecimal("500.00"), new BigDecimal("0.0"), null);
//        Order order = orderDao.findFullById(5L);
//        assertEquals(1, order.getPayments().size());
//        assertEquals(2, order.getKeys().size());
//        assertEquals(OrderStatus.PAYED, order.getStatus());
//    }

    @Test
    public void testListWithClient1() {
        prepareSecurity("firstUserName");
        prepareUserAccess(UserAccessCode.CLIENT_READ_PAYMENT, true);
        PaymentListParameters param = new PaymentListParameters();
        param.setUserId(((UserDetailsImpl) (SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal())).getUser().getId());
        paymentService.list(param);
    }

    @Test
    public void testListWithClient2() {
        prepareSecurity("firstUserName");
        prepareUserAccess(UserAccessCode.CLIENT_ADD_ORDER, true);
        PaymentListParameters param = new PaymentListParameters();
        param.setUserId(((UserDetailsImpl) (SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal())).getUser().getId());
        paymentService.list(param);
    }

    @Test(expected = AccessDeniedException.class)
    public void testListWithClient3() {
        prepareSecurity("firstUserName");
        prepareUserAccess(new UserAccessCode[] {
                UserAccessCode.CLIENT_READ_PAYMENT,
                UserAccessCode.CLIENT_ADD_ORDER }, false);
        PaymentListParameters param = new PaymentListParameters();
        param.setUserId(((UserDetailsImpl) (SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal())).getUser().getId());
        paymentService.list(param);
    }

    @Test
    public void testListWithService1() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_READ_PAYMENT, true);
        PaymentListParameters param = new PaymentListParameters();
        param.setUserId(((UserDetailsImpl) (SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal())).getUser().getId());
        paymentService.list(param);
    }

    @Test
    public void testListWithService2() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_EXECUTE_PAYMENT, true);
        PaymentListParameters param = new PaymentListParameters();
        param.setUserId(((UserDetailsImpl) (SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal())).getUser().getId());
        paymentService.list(param);
    }

    @Test
    public void testListWithService3() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_EXPORT_PAYMENT, true);
        PaymentListParameters param = new PaymentListParameters();
        param.setUserId(((UserDetailsImpl) (SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal())).getUser().getId());
        paymentService.list(param);
    }

    @Test(expected = AccessDeniedException.class)
    public void testListWithService4() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(new UserAccessCode[] {
                UserAccessCode.SERVICE_READ_PAYMENT,
                UserAccessCode.SERVICE_EXECUTE_PAYMENT,
                UserAccessCode.SERVICE_EXPORT_PAYMENT }, false);
        PaymentListParameters param = new PaymentListParameters();
        param.setUserId(((UserDetailsImpl) (SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal())).getUser().getId());
        paymentService.list(param);
    }

    @Test
    public void testCountListWithClient1() {
        prepareSecurity("firstUserName");
        prepareUserAccess(UserAccessCode.CLIENT_READ_PAYMENT, true);
        PaymentListParameters param = new PaymentListParameters();
        param.setUserId(((UserDetailsImpl) (SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal())).getUser().getId());
        paymentService.countList(param);
    }

    @Test
    public void testCountListWithClient2() {
        prepareSecurity("firstUserName");
        prepareUserAccess(UserAccessCode.CLIENT_ADD_ORDER, true);
        PaymentListParameters param = new PaymentListParameters();
        param.setUserId(((UserDetailsImpl) (SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal())).getUser().getId());
        paymentService.countList(param);
    }

    @Test(expected = AccessDeniedException.class)
    public void testCountListWithClient3() {
        prepareSecurity("firstUserName");
        prepareUserAccess(new UserAccessCode[] {
                UserAccessCode.CLIENT_READ_PAYMENT,
                UserAccessCode.CLIENT_ADD_ORDER }, false);
        PaymentListParameters param = new PaymentListParameters();
        param.setUserId(((UserDetailsImpl) (SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal())).getUser().getId());
        paymentService.countList(param);
    }

    @Test
    public void testCountListWithService1() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_READ_PAYMENT, true);
        PaymentListParameters param = new PaymentListParameters();
        param.setUserId(((UserDetailsImpl) (SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal())).getUser().getId());
        paymentService.countList(param);
    }

    @Test
    public void testCountListWithService2() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_EXECUTE_PAYMENT, true);
        PaymentListParameters param = new PaymentListParameters();
        param.setUserId(((UserDetailsImpl) (SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal())).getUser().getId());
        paymentService.countList(param);
    }

    @Test
    public void testCountListWithService3() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_EXPORT_PAYMENT, true);
        PaymentListParameters param = new PaymentListParameters();
        param.setUserId(((UserDetailsImpl) (SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal())).getUser().getId());
        paymentService.countList(param);
    }

    @Test(expected = AccessDeniedException.class)
    public void testCountListWithService4() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(new UserAccessCode[] {
                UserAccessCode.SERVICE_READ_PAYMENT,
                UserAccessCode.SERVICE_EXECUTE_PAYMENT,
                UserAccessCode.SERVICE_EXPORT_PAYMENT }, false);
        PaymentListParameters param = new PaymentListParameters();
        param.setUserId(((UserDetailsImpl) (SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal())).getUser().getId());
        paymentService.countList(param);
    }

    @Test(expected = AccessDeniedException.class)
    public void testCompleteWithClient1() {
        prepareSecurity("firstUserName");
        paymentService.complete(1L);
    }

    @Test
    public void testCompleteWithService1() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_EXECUTE_PAYMENT, true);
        paymentService.complete(1L);
    }

    @Test(expected = AccessDeniedException.class)
    public void testCompleteWithService2() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_EXECUTE_PAYMENT, false);
        paymentService.complete(1L);
    }
}
