package ru.urvanov.keystore.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.FileInputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.util.ReflectionTestUtils;

import ru.urvanov.keystore.domain.Client;
import ru.urvanov.keystore.domain.UserAccessCode;
import ru.urvanov.keystore.test.SpringTestBase;

public class MailServiceImplTest extends SpringTestBase {

    @Autowired
    private MailService mailService;

    @Autowired
    private DataSource dataSource;

    @Before
    public void before() throws Exception {

        // initialize your database connection here
        IDatabaseConnection databaseConnection = null;
        // ...

        // initialize your dataset here
        IDataSet dataSet = new FlatXmlDataSetBuilder()
                .build(new FileInputStream(
                        "src/test/java/ru/urvanov/keystore/service/MailServiceImplTest.xml"));

        try (Connection connection = dataSource.getConnection()) {
            databaseConnection = new DatabaseConnection(connection);
            DatabaseOperation.CLEAN_INSERT.execute(databaseConnection, dataSet);
        }
    }

    @Test
    public void testSendNotificationWithService1() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_SEND_NOTIFICATION, true);
        Client client = new Client();
        client.setContactPersonEmail("nowhere@nowhere.com");
        mailService.sendNotification(client, "test", "test");
    }

    @Test(expected = AccessDeniedException.class)
    public void testSendNotificationWithService2() {
        prepareSecurity("service@nowhere.com");
        prepareUserAccess(UserAccessCode.SERVICE_SEND_NOTIFICATION, false);
        Client client = new Client();
        client.setContactPersonEmail("nowhere@nowhere.com");
        mailService.sendNotification(client, "test", "test");
    }

    @Test
    public void testEvaluateSpEL1() {
        String text = "Simple text";
        Map<String, Object> variables = new HashMap<>();
        assertEquals(text, ReflectionTestUtils.invokeMethod(
                new MailServiceImpl(), "evaluateSpEL", text, variables));
    }

    @Test
    public void testEvaluateSpEL2() {
        class Obj1 {
            private String var1;

            public String getVar1() {
                return var1;
            }

            public void setVar1(String var1) {
                this.var1 = var1;
            }

        }

        String text = "Simple text${#obj1.var1}$";
        Map<String, Object> variables = new HashMap<>();
        Obj1 obj1 = new Obj1();
        obj1.setVar1("V1");
        variables.put("obj1", obj1);
        assertEquals("Simple textV1", ReflectionTestUtils.invokeMethod(
                new MailServiceImpl(), "evaluateSpEL", text, variables));
    }

    @Test
    public void testEvaluateSpEL3() {
        class Obj1 {
            private String field1;
            private BigDecimal field2;

            public String getField1() {
                return field1;
            }

            public void setField1(String field1) {
                this.field1 = field1;
            }

            public BigDecimal getField2() {
                return field2;
            }

            public void setField2(BigDecimal field2) {
                this.field2 = field2;
            }
        }
        String text = "Hello, ${#client.field1}$\r\nYour reward is ${#client.field2 == null ? \"0\" : #client.field2}$";
        Map<String, Object> variables = new HashMap<>();
        Obj1 obj1 = new Obj1();
        obj1.setField1("Vasya");
        variables.put("client", obj1);
        assertEquals("Hello, Vasya\r\nYour reward is 0",
                ReflectionTestUtils.invokeMethod(new MailServiceImpl(),
                        "evaluateSpEL", text, variables));

    }
    
    @Test
    public void testEvaluateSpEL5() {
        String text = "Simple ${obj.d.i}$ text";
        Map<String, Object> variables = new HashMap<>();
        assertEquals(text, ReflectionTestUtils.invokeMethod(
                new MailServiceImpl(), "evaluateSpEL", text, variables));
    }

}
