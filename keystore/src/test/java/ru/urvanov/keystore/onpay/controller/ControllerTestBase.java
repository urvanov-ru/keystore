package ru.urvanov.keystore.onpay.controller;

import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {
        "file:src/main/webapp/WEB-INF/spring/onpayServlet/onpay-servlet-context.xml",
        "file:src/main/webapp/WEB-INF/spring/root-context.xml" })
@ActiveProfiles("test")
public abstract class ControllerTestBase {


}
