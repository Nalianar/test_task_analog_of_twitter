package com.example.demo.service

import org.springframework.http.ResponseEntity

interface UserService {

    def updateUserById(String id, String username, String password)

    def deleteUserById(String id)

    def deleteUserByUsername(String username)
}