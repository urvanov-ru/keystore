package ru.urvanov.keystore.authentication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import ru.urvanov.keystore.domain.User;
import ru.urvanov.keystore.domain.UserDetailsImpl;
import ru.urvanov.keystore.exception.NotActivatedException;

public final class DatabaseAuthenticationProvider implements
        AuthenticationProvider {

    private static final Logger logger = LoggerFactory
            .getLogger(DatabaseAuthenticationProvider.class);

    private UserDetailsService userDetailsService;

    private BCryptPasswordEncoder bcryptEncoder;

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {
        logger.info("authenticate started.");
        String password = authentication.getCredentials().toString();
        String userName = authentication.getName();
        UserDetails userDetails = userDetailsService
                .loadUserByUsername(userName);
        User user = ((UserDetailsImpl) userDetails).getUser();
        if (user == null) {
            logger.error("User not found. UserName=" + userName);
            throw new BadCredentialsException(userName);
        } else if (!user.getActivated()) {
            logger.error("Not activated.");
            throw new NotActivatedException(userName);
        } else if (!user.getEnabled()) {
            logger.error("Not enabled.");
            throw new DisabledException(userName);
        } else {
            if (bcryptEncoder.matches(password, user.getPassword())) {
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                logger.info("authenticate finished.");
                return token;
            } else {
                logger.error("Password does not match. UserName=" + userName);
                throw new BadCredentialsException(userName);
            }

        }
    }

    @Override
    public boolean supports(Class<? extends Object> authentication) {
        return UsernamePasswordAuthenticationToken.class
                .isAssignableFrom(authentication);
    }

    public void setUserService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public void setBcryptEncoder(BCryptPasswordEncoder bcryptEncoder) {
        this.bcryptEncoder = bcryptEncoder;
    }

}
