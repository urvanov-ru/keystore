package ru.urvanov.keystore.service;

import static org.junit.Assert.assertEquals;

import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
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
import org.springframework.test.util.ReflectionTestUtils;

public class PayuServiceImplTest {

    @Autowired
    private PayuService payuService;

    @Autowired
    private DataSource dataSource;
    

    
    @Test
    public void testHashMd5() throws InvalidKeyException,
            UnsupportedEncodingException, NoSuchAlgorithmException {
        PayuServiceImpl payuServiceImpl = new PayuServiceImpl();
        ReflectionTestUtils.setField(payuServiceImpl, "secretKey",
                "AABBCCDDEEFF");
        assertEquals(
                "9c3858e32280011b119cf61bdcc12b92",
                payuServiceImpl.hashMd5("100500", "1", "Confirmed",
                        "2011-10-01 12:12:13").toLowerCase());
    }
}
