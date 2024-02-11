package com.example.demo.controller

import com.example.demo.dto.SignupDTO
import com.example.demo.service.AuthService
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController {

    @Autowired
    private AuthService authService

    @PostMapping("/signin")
    def authenticateUser(@RequestBody @NotBlank String username, @RequestBody @NotBlank String password ) {
        return authService.authenticateUser(username, password)
    }

    @PostMapping("/signup")
    def registerUser(@Valid @RequestBody SignupDTO signUpRequest) {
        return authService.registerUser(signUpRequest)
    }

    @PostMapping("/logout")
    def logoutUser (@RequestHeader("Authorization") String authorizationHeader){
        return authService.logoutUser(authorizationHeader)
    }
}
