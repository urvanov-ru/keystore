package ru.urvanov.keystore.api.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.FileInputStream;
import java.sql.Connection;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.commons.codec.binary.Base64;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import ru.urvanov.keystore.service.ClientUserDetailsServiceImpl;

public class ApiControllerTest extends ControllerTestBase {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private ClientUserDetailsServiceImpl clientUserDetailsServiceImpl;

    @Resource
    private FilterChainProxy springSecurityFilterChain;

    private MockMvc mockMvc;

    @Autowired
    private DataSource dataSource;
    
    @Before
    public void before() throws Exception {

        // initialize your database connection here
        IDatabaseConnection databaseConnection = null;
        // ...

        // initialize your dataset here
        FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
        builder.setColumnSensing(true);
        IDataSet dataSet = builder.build(new FileInputStream("src/test/java/ru/urvanov/keystore/api/controller/ApiControllerTest.xml"));

        try (Connection connection = dataSource.getConnection())
        {
            databaseConnection = new DatabaseConnection(connection);
            DatabaseOperation.CLEAN_INSERT.execute(databaseConnection, dataSet);
        }
    }
    
    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
                .addFilter(springSecurityFilterChain).build();
    }

    @Test
    public void keyList() throws Exception {
        String basicDigestHeaderValue = "Basic "
                + new String(Base64.encodeBase64(("DF4344:1").getBytes()));
        this.mockMvc
                .perform(
                        get("/api/key/list").servletPath("/api")
                                .header("Authorization", basicDigestHeaderValue)
                                .accept(MediaType
                                        .parseMediaType("application/json;charset=UTF-8")))
                .andExpect(status().isOk())
                .andExpect(
                        content().contentType("application/json;charset=UTF-8"))
                        .andExpect(content().string("{\"keys\":[{\"id\":\"1\","
                                + "\"dateBegin\":946674000000,\"dateEnd\":32503669200000,"
                                + "\"status\":\"CREATED\"},{\"id\":\"2\","
                                + "\"dateBegin\":946674000000,"
                                + "\"dateEnd\":949179600000,\"status\":\"ACTIVE\"},"
                                + "{\"id\":\"3\",\"dateBegin\":null,"
                                + "\"dateEnd\":null,\"status\":\"CREATED\"}]}"));
        // Дописать проверку результата.
    }
    
    @Test
    public void key() throws Exception {
        String basicDigestHeaderValue = "Basic "
                + new String(Base64.encodeBase64(("DF4344:1").getBytes()));
        this.mockMvc
                .perform(
                        get("/api/key?id=2").servletPath("/api")
                                .header("Authorization", basicDigestHeaderValue))
                .andExpect(status().isOk())
                        .andExpect(content().string("+oBHXaxrdQF1dGklbUBNbmwzypcA140irDYiBtD4wadRu5haNr3QGQym3vMYm8vwZd3h+KNWb8b1\n"+
"QIITsRrwcx+EQOlGL2l6EkEEvN3dNYpG0qI/A8ugTnlddciS8EuOUsQCH6iHjfruTtAH0k8ARFjD\n"+
"FIjwboXPnAUszpFR/z+ehtjQrFg5WBcJG35x22aeJ6/jfbQZM0JjLXRTyBKPLki3WiHd5td1wQp5\n"+
"8uh2JAgwdbWNoTBmC/Wn6mrlj6A6HpUW4KMvKCKDWJWPx06LZ0D/G8bTS2kwyHla0PU1RLbWgCDT\n"+
"KI7QgkUZfeD+IHx3DH98w+XskwntQ3xw/gYxSgx/fMPl7JMJGCP5PvVbwNQMf3zD5eyTCdYOb82C\n"+
"38MojiaqJKXtL2EwlV0WtF2cA3UuWM5RwIyDzs5yZqABGFhPXeh4SfD3Fp2T4ja5zgD9HgCwpbt2\n"+
"1RSAYmDBHLyE7sQvB7Wey4S9fgCJB4v8rO7ELwe1nsuEvVDgYbOCMw23DE5mJIj8JnsZq016MAzz\n"+
"/t1XbAstwS3Sl9mImqN0ae+DA5N/f1mt1CSuuz6gQ0LgdJdnbFzbjY07UXQ4wUMPGQ=="));
    }
}
