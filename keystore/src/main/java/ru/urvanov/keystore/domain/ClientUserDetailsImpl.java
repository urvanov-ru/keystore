package ru.urvanov.keystore.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class ClientUserDetailsImpl implements Serializable, UserDetails {

    /**
     * 
     */
    private static final long serialVersionUID = -253448992507513184L;
    private Client client;

    public ClientUserDetailsImpl() {

    }

    public ClientUserDetailsImpl(Client client) {
        if (client == null)
            throw new IllegalArgumentException();
        this.client = client;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> result = new ArrayList<SimpleGrantedAuthority>();
        SimpleGrantedAuthority sga = new SimpleGrantedAuthority("ROLE_API");
        result.add(sga);
        return result;

    }

    @Override
    public String getPassword() {
        return client == null ? null : client.getPassword();
    }

    @Override
    public String getUsername() {
        return client == null ? null : client.getUniqueId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return client != null;
    }

    @Override
    public boolean isAccountNonLocked() {
        return client != null;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return client != null;
    }

    @Override
    public boolean isEnabled() {
        return client != null;
    }

}
