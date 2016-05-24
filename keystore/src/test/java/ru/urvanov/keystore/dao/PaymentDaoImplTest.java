package ru.urvanov.keystore.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.FileInputStream;
import java.math.BigInteger;
import java.sql.Connection;
import java.util.Date;

import javax.sql.DataSource;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import ru.urvanov.keystore.domain.Payment;
import ru.urvanov.keystore.domain.PaymentListParameters;
import ru.urvanov.keystore.domain.PaymentType;
import ru.urvanov.keystore.test.SpringTestBase;

public class PaymentDaoImplTest  extends SpringTestBase {

    @Autowired
    private PaymentDao paymentDao;
    
    @Autowired
    private OrderDao orderDao;
    
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
        IDataSet dataSet = new FlatXmlDataSetBuilder().build(new FileInputStream("src/test/java/ru/urvanov/keystore/dao/PaymentDaoImplTest.xml"));

        try (Connection connection = dataSource.getConnection())
        {
            databaseConnection = new DatabaseConnection(connection);
            DatabaseOperation.CLEAN_INSERT.execute(databaseConnection, dataSet);
        }
    }
    
    @Test
    @Transactional
    public void testFindById1() {
        Payment payment = paymentDao.findById(1L);
        assertNotNull(payment);
        assertEquals(new Long(1L), payment.getId());
        assertEquals(PaymentType.ORDER_PAYMENT, payment.getPaymentType());
        assertNotNull(payment.getOrder());
        assertEquals(new Long(1L), payment.getOrder().getId());
    }
    
    @Test
    @Transactional
    public void testFindById2() {
        assertNull(paymentDao.findById(-1L));
    }
    
    @Test
    @Transactional
    public void save() {
        Payment payment = new Payment();
        payment.setOrder(orderDao.getReference(1L));
        payment.setCreatedAt(new Date());
    }
    
    @Test
    @Transactional
    public void testList1() {
        PaymentListParameters param = new PaymentListParameters();
        param.setUserId(1L);
        assertEquals(1, paymentDao.list(param).size());
    }
    
    @Test
    @Transactional
    public void testCountList1() {
        PaymentListParameters param = new PaymentListParameters();
        param.setUserId(1L);
        assertEquals(0, new BigInteger("1").compareTo(paymentDao.countList(param)));
    }
}
