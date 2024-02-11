package com.example.demo.dto

import groovy.transform.builder.Builder
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Builder
class UserDTO {

    String id
    String username
    String password
    Collection<? extends GrantedAuthority> authorities;

}
