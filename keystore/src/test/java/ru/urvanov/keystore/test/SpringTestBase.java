package ru.urvanov.keystore.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.dbunit.DataSourceBasedDBTestCase;
import org.dbunit.operation.DatabaseOperation;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ru.urvanov.keystore.domain.Authority;
import ru.urvanov.keystore.domain.ClientUserDetailsImpl;
import ru.urvanov.keystore.domain.User;
import ru.urvanov.keystore.domain.UserAccess;
import ru.urvanov.keystore.domain.UserAccessCode;
import ru.urvanov.keystore.domain.UserDetailsImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring/root-context.xml" })
@ActiveProfiles("test")
public abstract class SpringTestBase {

    @Autowired
    protected UserDetailsService userDetailsService;

    @Autowired
    private UserDetailsService clientUserDetailsService;

    @Autowired
    protected BCryptPasswordEncoder bcryptEncoder;

    protected void prepareSecurity(String userName) {
        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService
                .loadUserByUsername(userName);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                userDetails, userDetails.getPassword(),
                userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(token);
    }

    protected void prepareAnonymousSecurity() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("ANONYMOUS"));
        AnonymousAuthenticationToken token = new AnonymousAuthenticationToken(
                "anonymous", new Object(), authorities);
        SecurityContextHolder.getContext().setAuthentication(token);
    }

    protected void prepareUserAccess(UserAccessCode[] userAccessCodes,
            boolean access) {
        UserDetailsImpl userDetailsImpl = (UserDetailsImpl) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        Map<UserAccessCode, UserAccess> userAccesses = userDetailsImpl
                .getUser().getUserAccesses();
        for (UserAccessCode code : UserAccessCode.values()) {
            if (!userAccesses.containsKey(code)) {
                UserAccess userAccess = new UserAccess();
                userAccess.setCode(code);
                userAccess.setUser(userDetailsImpl.getUser());
                userAccesses.put(code, userAccess);
            }
            userAccesses.get(code).setAccess(!access);
        }
        for (UserAccessCode code : userAccessCodes) {
            userAccesses.get(code).setAccess(access);
        }
    }

    protected void prepareUserAccess(UserAccessCode userAccessCode,
            boolean access) {
        this.prepareUserAccess(new UserAccessCode[] { userAccessCode }, access);
    }

    protected void prepareApiSecurity(String userName) {
        ClientUserDetailsImpl userDetails = (ClientUserDetailsImpl) clientUserDetailsService
                .loadUserByUsername(userName);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                userDetails, userDetails.getPassword(),
                userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(token);
    }
    
    
    protected void prepareRolePaySecurity() {
        User user = new User();
        user.setActivated(true);
        Authority authority = new Authority();
        authority.setAuthority("ROLE_PAY");
        authority.setUser(user);
        user.getAuthorities().add(authority);
        UserDetailsImpl userDetails = new UserDetailsImpl(user);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                userDetails, userDetails.getPassword(),
                userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(token);
    }


}
