package ru.urvanov.keystore.controller;

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
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

public class ReportControllerTest extends ControllerTestBase {


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
        IDataSet dataSet = new FlatXmlDataSetBuilder().build(new FileInputStream("src/test/java/ru/urvanov/keystore/controller/ReportControllerTest.xml"));

        try (Connection connection = dataSource.getConnection())
        {
            databaseConnection = new DatabaseConnection(connection);
            DatabaseOperation.CLEAN_INSERT.execute(databaseConnection, dataSet);
        }
    }
    
    @Test
    public void testActivity1() throws Exception {
        prepareSecurity("service@nowhere.com");
        this.mockMvc
                .perform(
                        post("/site/report/activity")
                                .with(new UserDetailsRequestPostProcessor(
                                        "service@nowhere.com"))
                                .servletPath("/site")
                                .param("reportMode", "1")
                                .param("dateBegin", "2015-01-01")
                                .param("dateEnd", "2015-03-03")
                                )

                .andExpect(status().isOk())
                .andExpect(
                        content().contentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                ;
        
    }
    
    @Test
    public void testActivity2() throws Exception {
        prepareSecurity("service@nowhere.com");
        this.mockMvc
                .perform(
                        post("/site/report/activity")
                                .with(new UserDetailsRequestPostProcessor(
                                        "service@nowhere.com"))
                                .servletPath("/site")
                                .param("reportMode", "2")
                                .param("dateBegin", "2015-01-01")
                                .param("dateEnd", "2015-03-03")
                                )

                .andExpect(status().isOk())
                .andExpect(
                        content().contentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                ;
        
    }
    
    @Test
    public void testActivity3() throws Exception {
        prepareSecurity("service@nowhere.com");
        this.mockMvc
                .perform(
                        post("/site/report/activity")
                                .with(new UserDetailsRequestPostProcessor(
                                        "service@nowhere.com"))
                                .servletPath("/site")
                                .param("reportMode", "3")
                                .param("dateBegin", "2015-01-01")
                                .param("dateEnd", "2015-03-03")
                                )

                .andExpect(status().isOk())
                .andExpect(
                        content().contentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                ;
        
    }
    
    @Test
    public void testActivity4() throws Exception {
        prepareSecurity("service@nowhere.com");
        this.mockMvc
                .perform(
                        post("/site/report/activity")
                                .with(new UserDetailsRequestPostProcessor(
                                        "service@nowhere.com"))
                                .servletPath("/site")
                                .param("reportMode", "4")
                                .param("dateBegin", "2015-01-01")
                                .param("dateEnd", "2015-03-03")
                                )

                .andExpect(status().isOk())
                .andExpect(
                        content().contentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                ;
        
    }
    
    @Test
    public void testActivity5() throws Exception {
        prepareSecurity("service@nowhere.com");
        this.mockMvc
                .perform(
                        post("/site/report/activity")
                                .with(new UserDetailsRequestPostProcessor(
                                        "service@nowhere.com"))
                                .servletPath("/site")
                                .param("reportMode", "5")
                                .param("dateBegin", "2015-01-01")
                                .param("dateEnd", "2015-03-03")
                                )

                .andExpect(status().isOk())
                .andExpect(
                        content().contentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                ;
        
    }
    
    
    @Test
    public void testClientActivity1() throws Exception {
        prepareSecurity("service@nowhere.com");
        this.mockMvc
                .perform(
                        post("/site/report/activity")
                                .with(new UserDetailsRequestPostProcessor(
                                        "service@nowhere.com"))
                                .servletPath("/site")
                                .param("reportMode", "1")
                                .param("dateBegin", "2015-01-01")
                                .param("dateEnd", "2015-03-03")
                                .param("clientId", "1")
                                )

                .andExpect(status().isOk())
                .andExpect(
                        content().contentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                ;
        
    }
    
    @Test
    public void testClientActivity2() throws Exception {
        prepareSecurity("service@nowhere.com");
        this.mockMvc
                .perform(
                        post("/site/report/activity")
                                .with(new UserDetailsRequestPostProcessor(
                                        "service@nowhere.com"))
                                .servletPath("/site")
                                .param("reportMode", "2")
                                .param("dateBegin", "2015-01-01")
                                .param("dateEnd", "2015-03-03")
                                .param("clientId", "1")
                                )

                .andExpect(status().isOk())
                .andExpect(
                        content().contentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                ;
        
    }
    
    @Test
    public void testClientActivity3() throws Exception {
        prepareSecurity("service@nowhere.com");
        this.mockMvc
                .perform(
                        post("/site/report/activity")
                                .with(new UserDetailsRequestPostProcessor(
                                        "service@nowhere.com"))
                                .servletPath("/site")
                                .param("reportMode", "3")
                                .param("dateBegin", "2015-01-01")
                                .param("dateEnd", "2015-03-03")
                                .param("clientId", "1")
                                )

                .andExpect(status().isOk())
                .andExpect(
                        content().contentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                ;
        
    }
    
    @Test
    public void testClientActivity4() throws Exception {
        prepareSecurity("service@nowhere.com");
        this.mockMvc
                .perform(
                        post("/site/report/activity")
                                .with(new UserDetailsRequestPostProcessor(
                                        "service@nowhere.com"))
                                .servletPath("/site")
                                .param("reportMode", "4")
                                .param("dateBegin", "2015-01-01")
                                .param("dateEnd", "2015-03-03")
                                .param("clientId", "1")
                                )

                .andExpect(status().isOk())
                .andExpect(
                        content().contentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                ;
        
    }
    
    @Test
    public void testClientActivity5() throws Exception {
        prepareSecurity("service@nowhere.com");
        this.mockMvc
                .perform(
                        post("/site/report/activity")
                                .with(new UserDetailsRequestPostProcessor(
                                        "service@nowhere.com"))
                                .servletPath("/site")
                                .param("reportMode", "5")
                                .param("dateBegin", "2015-01-01")
                                .param("dateEnd", "2015-03-03")
                                .param("clientId", "1")
                                )

                .andExpect(status().isOk())
                .andExpect(
                        content().contentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                ;
        
    }
    
    @Test
    public void testPayment1() throws Exception {
        prepareSecurity("service@nowhere.com");
        this.mockMvc
                .perform(
                        post("/site/report/payment")
                                .with(new UserDetailsRequestPostProcessor(
                                        "service@nowhere.com"))
                                .servletPath("/site")
                                .param("reportMode", "1")
                                .param("dateBegin", "2015-01-01")
                                .param("dateEnd", "2015-03-03")
                                )

                .andExpect(status().isOk())
                .andExpect(
                        content().contentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                ;
        
    }

}
