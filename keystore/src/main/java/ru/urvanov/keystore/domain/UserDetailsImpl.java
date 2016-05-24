package ru.urvanov.keystore.domain;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserDetailsImpl implements UserDetails {

    /**
     * 
     */
    private static final long serialVersionUID = 2923088514040673155L;

    private User user;
    
    private Date authenticatedDate = new Date();

    public UserDetailsImpl() {
    }

    public UserDetailsImpl(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<SimpleGrantedAuthority> grantedAuthorities = new HashSet<SimpleGrantedAuthority>();
        for (Authority authority : this.user.getAuthorities()) {
            SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority(
                    authority.getAuthority());
            grantedAuthorities.add(grantedAuthority);
        }
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return user == null ? null : user.getPassword();
    }

    @Override
    public String getUsername() {
        return user == null ? null : user.getUserName();
    }

    public User getUser() {
        return user;
    }

    public Client getClient() {
        return user == null ? null : user.getClient();
    }

    public boolean hasAccess(UserAccessCode... userAccessCodes) {
        boolean result = false;
        for (UserAccessCode userAccessCode : userAccessCodes) {
            result = result
                    || (user == null ? false : user.getUserAccesses()
                            .containsKey(userAccessCode) ? user
                            .getUserAccesses().get(userAccessCode).getAccess()
                            : false);
        }
        return result;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user == null ? false : user.getEnabled() && user.getActivated();
    }

    public Date getAuthenticatedDate() {
        return authenticatedDate;
    }

}
