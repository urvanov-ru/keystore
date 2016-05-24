package ru.urvanov.keystore.filter;

import java.io.IOException;
import java.io.Writer;
import java.util.Properties;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import net.tanesha.recaptcha.ReCaptchaImpl;
import net.tanesha.recaptcha.ReCaptchaResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Filter for capturing Captcha fields. It's purpose is to store these values
 * internally
 */
public class CaptchaCaptureFilter extends OncePerRequestFilter {

    protected Logger logger = LoggerFactory
            .getLogger(CaptchaCaptureFilter.class);

    private String recaptcha_response;
    private String recaptcha_challenge;
    private String remoteAddr;

    private Boolean useProxy = false;
    private String proxyPort;
    private String proxyHost;
    private String failureUrl;
    private String privateKey;

    @Override
    public void doFilterInternal(HttpServletRequest req,
            HttpServletResponse res, FilterChain chain) throws IOException,
            ServletException {

        logger.debug("Captcha capture filter");

        // Assign values only when user has submitted a Captcha value.
        // Without this condition the values will be reset due to redirection
        // and CaptchaVerifierFilter will enter an infinite loop
        if (req.getParameter("recaptcha_response_field") != null
                && !"".equals(req.getParameter("recaptcha_response_field"))) {
            recaptcha_response = req.getParameter("recaptcha_response_field");
            recaptcha_challenge = req.getParameter("recaptcha_challenge_field");
            remoteAddr = req.getRemoteAddr();

            // Create a new recaptcha (by Soren Davidsen)
            ReCaptchaImpl reCaptcha = new ReCaptchaImpl();

            // Set the private key (assigned by Google)
            reCaptcha.setPrivateKey(privateKey);

            // Assign proxy if needed
            if (useProxy) {
                Properties systemSettings = System.getProperties();
                systemSettings.put("http.proxyPort", proxyPort);
                systemSettings.put("http.proxyHost", proxyHost);
            }

            // Send HTTP request to validate user's Captcha
            ReCaptchaResponse reCaptchaResponse = reCaptcha.checkAnswer(
                    getRemoteAddr(), getRecaptcha_challenge(),
                    getRecaptcha_response());

            // Reset Captcha fields after processing
            // If this method is skipped, everytime we access a page
            // CaptchaVerifierFilter will infinitely send a request to the
            // Google Captcha service!
            resetCaptchaFields();

            // Check if valid
            if (!reCaptchaResponse.isValid()) {
                logger.debug("Captcha is invalid!");

                // Redirect user to login page
                // failureHandler.setDefaultFailureUrl(failureUrl);
                // failureHandler.onAuthenticationFailure(req, res,
                // new BadCredentialsException("Captcha invalid!"));
                HttpServletResponseWrapper responseWrapper = new HttpServletResponseWrapper(
                        res);
                Writer out = responseWrapper.getWriter();
                out.write("{success:false, errors: {code : 'captcha'}}");
                out.close();
                return;
            } else {
                logger.debug("Captcha is valid!");
            }
        }

        logger.debug("challenge: " + recaptcha_challenge);
        logger.debug("response: " + recaptcha_response);
        logger.debug("remoteAddr: " + remoteAddr);

        // Proceed with the remaining filters
        chain.doFilter(req, res);
    }

    public String getRecaptcha_response() {
        return recaptcha_response;
    }

    public void setRecaptcha_response(String recaptchaResponse) {
        recaptcha_response = recaptchaResponse;
    }

    public String getRecaptcha_challenge() {
        return recaptcha_challenge;
    }

    public void setRecaptcha_challenge(String recaptchaChallenge) {
        recaptcha_challenge = recaptchaChallenge;
    }

    public String getRemoteAddr() {
        return remoteAddr;
    }

    public void setRemoteAddr(String remoteAddr) {
        this.remoteAddr = remoteAddr;
    }

    /**
     * Reset Captcha fields
     */
    public void resetCaptchaFields() {
        setRemoteAddr(null);
        setRecaptcha_challenge(null);
        setRecaptcha_response(null);
    }

    public Boolean getUseProxy() {
        return useProxy;
    }

    public void setUseProxy(Boolean useProxy) {
        this.useProxy = useProxy;
    }

    public String getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(String proxyPort) {
        this.proxyPort = proxyPort;
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    public String getFailureUrl() {
        return failureUrl;
    }

    public void setFailureUrl(String failureUrl) {
        this.failureUrl = failureUrl;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }
}