package ru.urvanov.keystore.dao;

import static org.junit.Assert.assertNotNull;

import java.io.FileInputStream;
import java.sql.Connection;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;

import javax.sql.DataSource;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import ru.urvanov.keystore.domain.ReportActivityParameters;
import ru.urvanov.keystore.domain.ReportPaymentParameters;
import ru.urvanov.keystore.test.SpringTestBase;

public class ReportDaoImplTest extends SpringTestBase {

    @Autowired
    private  ReportDao reportDao;
    
    @Autowired
    private DataSource dataSource;
    

    public void prepareDatabase(String filePath) throws Exception {

        // initialize your database connection here
        IDatabaseConnection databaseConnection = null;
        // ...

        // initialize your dataset here
        IDataSet dataSet = new FlatXmlDataSetBuilder().build(new FileInputStream(filePath));

        try (Connection connection = dataSource.getConnection())
        {
            databaseConnection = new DatabaseConnection(connection);
            DatabaseOperation.CLEAN_INSERT.execute(databaseConnection, dataSet);
        }
    }
    
    @Test
    @Transactional
    public void testReportActivity1() throws Exception {
        prepareDatabase("src/test/java/ru/urvanov/keystore/dao/ReportDaoImpl_ActivityTest.xml");
        ReportActivityParameters param = new ReportActivityParameters();
        param.setUserId(1L);
        ZonedDateTime zonedDateBegin = ZonedDateTime.of(2015, 1,1, 0, 0 ,0, 0, ZoneOffset.systemDefault());
        Date dateBegin = new Date();
        dateBegin.setTime(zonedDateBegin.toInstant().getEpochSecond());
        param.setDateBegin(dateBegin);
        ZonedDateTime zonedDateEnd = ZonedDateTime.of(2015,3,3,0,0,0,0, ZoneOffset.systemDefault());
        Date dateEnd = new Date();
        dateEnd.setTime(zonedDateEnd.toInstant().getEpochSecond());
        param.setDateEnd(dateEnd);
        param.setReportMode(ReportActivityParameters.REPORT_MODE_MONTH);
        assertNotNull(reportDao.activity(param));
    }
    
    @Test
    @Transactional
    public void testReportPayment1() throws Exception {
        prepareDatabase("src/test/java/ru/urvanov/keystore/dao/ReportDaoImpl_PaymentTest.xml");
        ReportPaymentParameters param = new ReportPaymentParameters();
        param.setUserId(1L);
        ZonedDateTime zonedDateBegin = ZonedDateTime.of(2015, 1,1, 0, 0 ,0, 0, ZoneOffset.systemDefault());
        Date dateBegin = new Date();
        dateBegin.setTime(zonedDateBegin.toInstant().getEpochSecond());
        param.setDateBegin(dateBegin);
        ZonedDateTime zonedDateEnd = ZonedDateTime.of(2015,3,3,0,0,0,0, ZoneOffset.systemDefault());
        Date dateEnd = new Date();
        dateEnd.setTime(zonedDateEnd.toInstant().getEpochSecond());
        param.setDateEnd(dateEnd);
        param.setReportMode(ReportActivityParameters.REPORT_MODE_MONTH);
        assertNotNull(reportDao.payment(param));
    }

}
