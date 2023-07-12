package com.yotsuki.boot.configJwt;

import com.yotsuki.boot.configJwt.model.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class UserDetailsImp implements UserDetails {
    private Long id;
    private String username;

    public UserDetailsImp(Long id, String username) {
        this.id = id;
        this.username = username;
    }

    public Long getId() {
        return this.id;
    }

    public static UserDetailsImp build(Authentication authentication) {
        return new UserDetailsImp(
                authentication.getId(),
                authentication.getUsername()
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
