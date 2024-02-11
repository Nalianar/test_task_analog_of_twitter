package com.example.demo.service

import com.example.demo.dto.SignupDTO

interface AuthService {

    def authenticateUser(String username, String password)

    def registerUser(SignupDTO signupDTO)

    def logoutUser(String s)
}