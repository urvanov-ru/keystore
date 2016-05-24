package ru.urvanov.keystore.filter;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import ru.urvanov.keystore.exception.NotActivatedException;

public class JsonAuthenticationFilter extends
        UsernamePasswordAuthenticationFilter {

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
            HttpServletResponse response, FilterChain filterChain,
            Authentication authResult) throws IOException, ServletException {
        SavedRequestAwareAuthenticationSuccessHandler srh = new SavedRequestAwareAuthenticationSuccessHandler();
        this.setAuthenticationSuccessHandler(srh);
        srh.setRedirectStrategy(new RedirectStrategy() {
            @Override
            public void sendRedirect(HttpServletRequest httpServletRequest,
                    HttpServletResponse httpServletResponse, String s)
                    throws IOException {
                // do nothing, no redirect
            }
        });
        super.successfulAuthentication(request, response, filterChain,
                authResult);
        HttpServletResponseWrapper responseWrapper = new HttpServletResponseWrapper(
                response);
        Writer out = responseWrapper.getWriter();
        out.write("{success:true}");
        out.close();
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
            HttpServletResponse response, AuthenticationException failed)
            throws IOException, ServletException {
        this.setAuthenticationFailureHandler((req, resp, e) -> {
        });
        super.unsuccessfulAuthentication(request, response, failed);
        HttpServletResponseWrapper responseWrapper = new HttpServletResponseWrapper(
                response);
        Writer out = responseWrapper.getWriter();
        if (failed instanceof BadCredentialsException) {
            out.write("{success:false, errors : {code : 'usernamepassword', reason : ''}}");
        } else if (failed instanceof NotActivatedException) {
            out.write("{success:false, errors : {code : 'notactivated', reason : ''}}");
        } else if (failed instanceof DisabledException) {
            out.write("{success:false, errors : {code : 'disabled', reason : ''}}");
        } else {
            out.write("{success:false, errors : {code : 'internalerror', reason : ''}}");
        }
        out.close();
    }
}