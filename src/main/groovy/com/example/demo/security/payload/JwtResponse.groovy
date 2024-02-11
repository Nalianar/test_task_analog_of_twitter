package com.example.demo.security.payload

import groovy.transform.EqualsAndHashCode

@EqualsAndHashCode
class JwtResponse {

    String token
    String type = "Bearer"
    String id
    String username
    List<String> roles
}
