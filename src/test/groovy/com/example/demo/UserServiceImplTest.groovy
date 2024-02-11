package com.example.demo

import com.example.demo.model.User
import com.example.demo.repository.UserRepository
import com.example.demo.service.impl.UserServiceImpl
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import spock.lang.Specification

class UserServiceImplTest extends Specification {

    UserRepository userRepository
    PasswordEncoder passwordEncoder

    UserServiceImpl classUnderTest

    def setup(){
        userRepository = Mock()
        passwordEncoder = Mock()
        classUnderTest = new UserServiceImpl(userRepository, passwordEncoder)
    }

    def "updateUserById: should return ok status"(){
        given:
        def id = "test"
        def username = "test"
        def password = "test"

        when:
        ResponseEntity response = classUnderTest.updateUserById(id, username, password)

        then:
        1 * userRepository.findById(id) >> Optional.of(new User(id: id, username: "username", password: "password"))
        1 * userRepository.save(_) >> new User(id: id, username: username, password: password)
        response.getStatusCode() == ResponseEntity.ok().build().getStatusCode()
    }

    def "updateUserById: should should catch exception and return bad request"(){
        given:
        def id = "test"
        def username = "test"
        def password = "test"

        when:
        ResponseEntity response = classUnderTest.updateUserById(id, username, password)

        then:
        1 * userRepository.findById(id) >> Optional.empty()
        response.getStatusCode() == ResponseEntity.badRequest().build().getStatusCode()
    }

    def "updateUserById should return bad request username is taken"(){
        given:
        def id = "test"
        def username = "test"
        def password = "test"

        when:
        ResponseEntity response = classUnderTest.updateUserById(id, username, password)

        then:
        1 * userRepository.findById(id) >> Optional.of(new User(id: id, username: username, password: "password"))
        1 * userRepository.existsByUsername(username) >> true
        response.getStatusCode() == ResponseEntity.badRequest().build().getStatusCode()
        response.getBody() == "This username is taken"
    }

    def "deleteUserById: should return response status ok"(){
        given:
        def id = "test"

        when:
        ResponseEntity response = classUnderTest.deleteUserById(id)

        then:
        1 * userRepository.findById(id) >> Optional.of(new User(id: id, username: "username", password: "password"))
        response.getStatusCode() == ResponseEntity.ok().build().getStatusCode()
    }

    def "deleteUserById: should return response status bad request"(){
        given:
        def id = "test"

        when:
        ResponseEntity response = classUnderTest.deleteUserById(id)

        then:
        1 * userRepository.findById(id) >> Optional.empty()
        response.getStatusCode() == ResponseEntity.badRequest().build().getStatusCode()
    }

    def "deleteUserByUsername: should return response status ok"(){
        given:
        def name = "test"

        when:
        ResponseEntity response = classUnderTest.deleteUserByUsername(name)

        then:
        1 * userRepository.findByUsername(name) >> Optional.of(new User(id: "id", username: name, password: "password"))
        response.getStatusCode() == ResponseEntity.ok().build().getStatusCode()
    }

    def "deleteUserByUsername: should return response status bad request"(){
        given:
        def name = "test"

        when:
        ResponseEntity response = classUnderTest.deleteUserByUsername(name)

        then:
        1 * userRepository.findByUsername(name) >> Optional.empty()
        response.getStatusCode() == ResponseEntity.badRequest().build().getStatusCode()
    }
}
