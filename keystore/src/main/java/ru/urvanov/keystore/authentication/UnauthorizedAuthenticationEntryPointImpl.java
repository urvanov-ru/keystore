package ru.urvanov.keystore.authentication;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

public class UnauthorizedAuthenticationEntryPointImpl implements
        AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory
            .getLogger(UnauthorizedAuthenticationEntryPointImpl.class);

    private AuthenticationEntryPoint loginUrlAuthenticationEntryPoint;

    @Override
    public void commence(HttpServletRequest request,
            HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        logger.debug("requestURI = {}", request.getRequestURI());
        logger.debug("requestURL = {}", request.getRequestURL());
        logger.debug("contextPath = {}", request.getContextPath());
        if ("".equals(request.getServletPath())
                || "/".equals(request.getServletPath()))
            loginUrlAuthenticationEntryPoint.commence(request, response,
                    authException);
        else
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }

    public AuthenticationEntryPoint getLoginUrlAuthenticationEntryPoint() {
        return loginUrlAuthenticationEntryPoint;
    }

    public void setLoginUrlAuthenticationEntryPoint(
            AuthenticationEntryPoint loginUrlAuthenticationEntryPoint) {
        this.loginUrlAuthenticationEntryPoint = loginUrlAuthenticationEntryPoint;
    }

}
