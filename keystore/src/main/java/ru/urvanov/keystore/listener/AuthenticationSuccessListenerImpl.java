package ru.urvanov.keystore.listener;

import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.UserDetails;

import ru.urvanov.keystore.domain.UserDetailsImpl;

public class AuthenticationSuccessListenerImpl implements ApplicationListener<AuthenticationSuccessEvent> {


    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent appEvent) {
        AuthenticationSuccessEvent event = (AuthenticationSuccessEvent) appEvent;
        UserDetails userDetails = (UserDetails) event.getAuthentication().getPrincipal();
        if (userDetails instanceof UserDetailsImpl) {
            
        }
    }

}
