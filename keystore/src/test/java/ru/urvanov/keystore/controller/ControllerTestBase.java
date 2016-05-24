package ru.urvanov.keystore.controller;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import ru.urvanov.keystore.domain.UserAccess;
import ru.urvanov.keystore.domain.UserAccessCode;
import ru.urvanov.keystore.domain.UserDetailsImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {
        "file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml",
        "file:src/main/webapp/WEB-INF/spring/root-context.xml" })
@ActiveProfiles("test")
public abstract class ControllerTestBase {

    @Autowired
    protected UserDetailsService userDetailsService;

    protected UsernamePasswordAuthenticationToken prepareSecurity(
            String userName) {
        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService
                .loadUserByUsername(userName);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                userDetails, userDetails.getPassword(),
                userDetails.getAuthorities());
        for (UserAccessCode userAccessCode : UserAccessCode.values()) {
            UserAccess userAccess = userDetails.getUser().getUserAccesses()
                    .get(userAccessCode);
            if (userAccess == null) {
                userAccess = new UserAccess();
                userAccess.setCode(userAccessCode);
                userAccess.setUser(userDetails.getUser());
            }
            userAccess.setAccess(true);
            userDetails.getUser().getUserAccesses()
                    .put(userAccessCode, userAccess);
        }

        return token;
    }

    protected class UserDetailsRequestPostProcessor extends
            SecurityContextRequestPostProcessorSupport implements
            RequestPostProcessor {

        private final String username;

        protected UserDetailsRequestPostProcessor(String username) {
            this.username = username;
        }

        public MockHttpServletRequest postProcessRequest(
                MockHttpServletRequest request) {
            UsernamePasswordAuthenticationToken authentication = authentication(request
                    .getServletContext());
            save(authentication, request);
            return request;
        }

        private UsernamePasswordAuthenticationToken authentication(
                ServletContext servletContext) {
            return prepareSecurity(username);
        }
    }

    /**
     * Support class for {@link RequestPostProcessor}'s that establish a Spring
     * Security context
     */
    private static abstract class SecurityContextRequestPostProcessorSupport {

        private SecurityContextRepository repository = new HttpSessionSecurityContextRepository();

        final void save(Authentication authentication,
                HttpServletRequest request) {
            SecurityContext securityContext = SecurityContextHolder
                    .createEmptyContext();
            securityContext.setAuthentication(authentication);
            save(securityContext, request);
        }

        final void save(SecurityContext securityContext,
                HttpServletRequest request) {
            HttpServletResponse response = new MockHttpServletResponse();

            HttpRequestResponseHolder requestResponseHolder = new HttpRequestResponseHolder(
                    request, response);
            this.repository.loadContext(requestResponseHolder);

            request = requestResponseHolder.getRequest();
            response = requestResponseHolder.getResponse();

            this.repository.saveContext(securityContext, request, response);
        }
    }

}
