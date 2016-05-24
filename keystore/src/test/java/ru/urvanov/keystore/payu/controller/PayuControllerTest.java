package ru.urvanov.keystore.payu.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.FileInputStream;
import java.sql.Connection;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

public class PayuControllerTest extends ControllerTestBase {

    @Autowired
    private WebApplicationContext wac;

    @Resource
    private FilterChainProxy springSecurityFilterChain;

    private MockMvc mockMvc;

    @Autowired
    private DataSource dataSource;
    
    @Before
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
                .addFilter(springSecurityFilterChain).build();

        // initialize your database connection here
        IDatabaseConnection databaseConnection = null;
        // ...

        // initialize your dataset here
        IDataSet dataSet = new FlatXmlDataSetBuilder().build(new FileInputStream("src/test/java/ru/urvanov/keystore/payu/controller/PayuControllerTest.xml"));

        try (Connection connection = dataSource.getConnection())
        {
            databaseConnection = new DatabaseConnection(connection);
            DatabaseOperation.CLEAN_INSERT.execute(databaseConnection, dataSet);
        }
    }

    @Test
    public void liveUpdate() throws Exception {
        this.mockMvc
                .perform(
                        post("/payu/ipn")
                                .servletPath("/payu")
                                .param("SALEDATE", "2012-04-04 12:04:26")
                                .param("REFNO", "6360085")
                                .param("REFNOEXT", "123456")
                                .param("ORDERNO", "34")
                                .param("ORDERSTATUS", "TEST")
                                .param("PAYMETHOD", "Visa/MasterCard/Eurocard")
                                .param("FIRSTNAME", "test")
                                .param("LASTNAME", "testov")
                                .param("COUNTRY", "Russia")
                                .param("PHONE", "+7(910)123-45-67")
                                .param("CUSTOMEREMAIL", "test@test.com")
                                .param("FIRSTNAME_D", "test")
                                .param("LASTNAME_D", "testov")
                                .param("COUNTRY_D", "Russia")
                                .param("PHONE_D", "+7(910)123-45-67")
                                .param("IPADDRESS", "84.253.97.138")
                                .param("CURRENCY", "RUB")
                                .param("IPN_PID[]", "172321")
                                .param("IPN_PNAME[]", "some_name")
                                .param("IPN_PCODE[]", "some_code")
                                .param("IPN_INFO[]", "some_info")
                                .param("IPN_QTY[]", "1")
                                .param("IPN_PRICE[]", "99.19")
                                .param("IPN_VAT[]", "23.81")
                                .param("IPN_DISCOUNT[]", "0.00")
                                .param("IPN_TOTAL[]", "123.00")
                                .param("IPN_TOTALGENERAL", "123.00")
                                .param("IPN_SHIPPING", "0.00")
                                .param("IPN_COMMISSION", "0.00")
                                .param("IPN_DATE", "20120404122732")
                                .param("HASH",
                                        "9a4c1487ee7168cba345b9d2ef12cc5d")
                                .accept(MediaType
                                        .parseMediaType("application/json;charset=UTF-8")))

                .andExpect(status().isOk())
                .andExpect(
                        content().contentType("application/json;charset=UTF-8"));
    }
}
