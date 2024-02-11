package com.example.demo.dto

import groovy.transform.builder.Builder
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Builder
class AuthDTO implements UserDetails {

    String id
    String username
    String password
    Collection<? extends GrantedAuthority> authorities;

    @Override
    boolean isAccountNonExpired() {
        return true
    }

    @Override
    boolean isAccountNonLocked() {
        return true
    }

    @Override
    boolean isCredentialsNonExpired() {
        return true
    }

    @Override
    boolean isEnabled() {
        return true
    }
}
