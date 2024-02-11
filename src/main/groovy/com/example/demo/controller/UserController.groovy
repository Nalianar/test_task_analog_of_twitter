package com.example.demo.controller

import com.example.demo.service.UserService
import jakarta.validation.constraints.NotBlank
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserController {

    @Autowired
    private UserService userService

    @PutMapping("/update/{id}")
    def updateUserById(@PathVariable String id, @RequestParam @NotBlank String username, @RequestParam @NotBlank String password){
        return userService.updateUserById(id, username, password)
    }

    @DeleteMapping("/delete/{id}")
    def deleteUserById(@PathVariable String id) {
        return userService.deleteUserById(id)
    }

    @DeleteMapping("/delete/username/{username}")
    def deleteUserByUsername(@PathVariable String username) {
        return userService.deleteUserByUsername(username)
    }
}
