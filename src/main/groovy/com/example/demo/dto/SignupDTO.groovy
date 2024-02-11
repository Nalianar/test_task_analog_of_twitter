package com.example.demo.dto

import groovy.transform.ToString
import groovy.transform.builder.Builder
import jakarta.validation.constraints.NotBlank

@ToString(includeNames=true)
@Builder
class SignupDTO {
    @NotBlank
    String username

    Set<String> roles

    @NotBlank
    String password
}
