package ru.urvanov.keystore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ru.urvanov.keystore.dao.ClientDao;
import ru.urvanov.keystore.domain.ClientUserDetailsImpl;

@Service("clientUserDetailsService")
public class ClientUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private ClientDao clientDao;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        return new ClientUserDetailsImpl(clientDao.findByUniqueId(username));
    }

}
