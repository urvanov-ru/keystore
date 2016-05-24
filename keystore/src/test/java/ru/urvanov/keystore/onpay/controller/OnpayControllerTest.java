package ru.urvanov.keystore.onpay.controller;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
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

import ru.urvanov.keystore.dao.OrderDao;
import ru.urvanov.keystore.domain.Order;
import ru.urvanov.keystore.domain.OrderStatus;
import ru.urvanov.keystore.onpay.controller.OnpayController.MerchantResponse;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OnpayControllerTest extends ControllerTestBase {

    @Autowired
    private WebApplicationContext wac;

    @Resource
    private FilterChainProxy springSecurityFilterChain;

    private MockMvc mockMvc;

    @Autowired
    private DataSource dataSource;
    
    @Autowired
    private OrderDao orderDao;
    
    @Before
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
                .addFilter(springSecurityFilterChain).build();

        // initialize your database connection here
        IDatabaseConnection databaseConnection = null;
        // ...

        // initialize your dataset here
        IDataSet dataSet = new FlatXmlDataSetBuilder().build(new FileInputStream("src/test/java/ru/urvanov/keystore/onpay/controller/OnpayControllerTest.xml"));

        try (Connection connection = dataSource.getConnection())
        {
            databaseConnection = new DatabaseConnection(connection);
            DatabaseOperation.CLEAN_INSERT.execute(databaseConnection, dataSet);
        }
    }

    @Test
    public void testCheck1() throws Exception {
        this.mockMvc
                .perform(
                        post("/onpay/check")
                                .servletPath("/onpay").content("{\"type\":\"check\",\"pay_for\":\"4\",\"amount\":1234.0,\"way\":\"RUR\",\"mode\":\"fix\",\"signature\":\"e4a828fd985515d0edeba3a93a2f2a28d4393fcf\"}")
                                .contentType(MediaType
                                        .parseMediaType("application/json;charset=UTF-8"))
                                .accept(MediaType
                                        .parseMediaType("application/json;charset=UTF-8")))

                .andExpect(status().isOk())
                .andExpect(
                        content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().string("{\"status\":true,\"signature\":\"a140613a71626f992f52372e42ca2ecf30c159f5\",\"pay_for\":\"4\"}"));
        
        
    }
    
    @Test
    public void testPay1() throws Exception {
        this.mockMvc
        .perform(
                post("/onpay/check")
                        .servletPath("/onpay").content("{\"type\":\"pay\", \"signature\":\"51820d5002e3f67b6e28a60e66dbda88edcf5499\", \"pay_for\":\"4\", \"user\":{\"email\":\"\", \"phone\":\"\", \"note\":\"\"}, \"payment\":{\"id\":12194597, \"date_time\":\"2015-02-26 184814 +0300\", \"amount\":1234.0, \"way\":\"RUR\", \"rate\":1.0, \"release_at\":\"null\"}, \"balance\":{\"amount\":1234.0, \"way\":\"RUR\"}}")
                        .contentType(MediaType
                                .parseMediaType("application/json;charset=UTF-8"))
                        .accept(MediaType
                                .parseMediaType("application/json;charset=UTF-8")))

        .andExpect(status().isOk())
        .andExpect(
                content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().string("{\"status\":true,\"signature\":\"fe8fbc8d823fed15e6bb57720cd5ddae11f0f800\",\"pay_for\":\"4\"}"));
        Order order = orderDao.findFullById(4L);
        assertEquals(OrderStatus.PAYED, order.getStatus());
        assertEquals(1, order.getPayments().size());
    }
    
    @Test
    public void testMerchantResponseDeserialize1() throws JsonGenerationException, JsonMappingException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        MerchantResponse merchantResponse = new MerchantResponse();
        merchantResponse.setPayFor("mypayfor");
        merchantResponse.setSignature("mysignature");
        merchantResponse.setStatus(true);
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            objectMapper.writeValue(os, merchantResponse);
            byte[] byteArray = os.toByteArray();
            String json = new String(byteArray, "UTF-8");
            assertEquals("{\"status\":true,\"signature\":\"mysignature\",\"pay_for\":\"mypayfor\"}", json);
        }
    }
}
