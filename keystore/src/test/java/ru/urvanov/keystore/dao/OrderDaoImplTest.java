package ru.urvanov.keystore.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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
import org.springframework.transaction.annotation.Transactional;

import ru.urvanov.keystore.dao.DictServiceTypeDao;
import ru.urvanov.keystore.dao.OrderDao;
import ru.urvanov.keystore.dao.UserDao;
import ru.urvanov.keystore.domain.KeyActivationMode;
import ru.urvanov.keystore.domain.Order;
import ru.urvanov.keystore.domain.OrderListParameters;
import ru.urvanov.keystore.domain.OrderStatus;
import ru.urvanov.keystore.test.SpringTestBase;

public class OrderDaoImplTest extends SpringTestBase {

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
        IDataSet dataSet = new FlatXmlDataSetBuilder().build(new FileInputStream("src/test/java/ru/urvanov/keystore/dao/OrderDaoImplTest.xml"));

        try (Connection connection = dataSource.getConnection())
        {
            databaseConnection = new DatabaseConnection(connection);
            DatabaseOperation.CLEAN_INSERT.execute(databaseConnection, dataSet);
        }
    }
    
    @Test
    @Transactional
    public void testFindById1() {
        Order order = orderDao.findById(1L);
        assertNotNull(order);
        assertEquals(KeyActivationMode.AUTOMATIC, order.getKeyActivationMode());
        assertEquals(new Long(1L), order.getId());
        assertEquals(OrderStatus.COMPLETED, order.getStatus());
    }
    
    @Test
    @Transactional
    public void testFindById2() {
        Order order = orderDao.findById(-1L);
        assertNull(order);
    }
    
    @Test
    @Transactional
    public void testGetReference() {
        Order order = orderDao.getReference(1L);
        assertNotNull(order);
        assertEquals(new Long(1L), order.getId());
    }
    
    @Test
    @Transactional
    public void testFindFullById1() {
        Order order = orderDao.findFullById(1L);
        assertNotNull(order);
        assertNotNull(order.getPayments());
        assertEquals(1, order.getPayments().size());
        assertEquals(1, order.getKeys().size());
    }
    
    @Test
    @Transactional
    public void testFindFullById2() {
        Order order = orderDao.findFullById(2L);
        assertNotNull(order);
        assertNotNull(order.getKeys());
        assertNotNull(order.getPayments());
        assertEquals(0, order.getPayments().size());
        assertEquals(0, order.getKeys().size());
    }
    
    @Test
    @Transactional
    public void testFindFullbyId2() {
        assertNull(orderDao.findFullById(-1L));
    }
    
    @Test
    @Transactional
    public void testSave() {
        Order order =new Order();
        Date nowDate = new Date();
        order.setCreatedAt(nowDate);
        order.setCreatedBy(userDao.getReference(1L));
        order.setDictServiceType(dictServiceTypeDao.getReference(1L));
        order.setKeyActivationMode(KeyActivationMode.MANUAL);
        order.setPayDateTime(nowDate);
        order.setStatus(OrderStatus.PENDING_PAYMENT);
        orderDao.save(order);
        Long id = order.getId();
        assertNotNull(id);
        order = orderDao.findFullById(id);
        assertNotNull(order);
        assertNotNull(order.getPayments());
        assertNotNull(order.getKeys());
        assertEquals(OrderStatus.PENDING_PAYMENT, order.getStatus());
        assertEquals(nowDate, order.getPayDateTime());
        assertEquals(KeyActivationMode.MANUAL, order.getKeyActivationMode());
        assertNotNull(order.getDictServiceType());
        assertEquals(new Long(1L), order.getDictServiceType().getId());
        assertNotNull(order.getCreatedBy());
        assertEquals(new Long(1L), order.getCreatedBy().getId());
        assertEquals(nowDate, order.getCreatedAt());
    }
    
    @Test
    @Transactional
    public void testList1() {
        OrderListParameters params = new OrderListParameters();
        params.setUserId(1L);
        assertEquals(2, orderDao.list(params).size());
    }
    
    @Test
    @Transactional
    public void testList2() {
        OrderListParameters param = new OrderListParameters() ;
        param.setUserId(234234L);
        param.setClientName("dfadf");
        param.setStatus(OrderStatus.PAY_BACK);
        param.setCreatedAtBegin(new Date());
        param.setCreatedAtEnd(new Date());
        assertEquals(0, orderDao.list(param).size());
    }
    
    @Test
    @Transactional
    public void testCountList() {
        OrderListParameters param = new OrderListParameters() ;
        param.setUserId(234234L);
        param.setClientName("dfadFFFFFFFFFFFFFFFFFFFSDSDSDSf");
        param.setStatus(OrderStatus.PAY_BACK);
        param.setCreatedAtBegin(new Date());
        param.setCreatedAtEnd(new Date());
        assertEquals(0, orderDao.countList(param).longValue());
    }
}
