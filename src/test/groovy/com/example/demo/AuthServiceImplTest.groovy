package com.example.demo

import com.example.demo.dto.AuthDTO
import com.example.demo.dto.SignupDTO
import com.example.demo.model.EnumRole
import com.example.demo.model.Role
import com.example.demo.model.User
import com.example.demo.repository.RoleRepository
import com.example.demo.repository.UserRepository
import com.example.demo.security.jwt.JwtUtils
import com.example.demo.security.payload.JwtResponse
import com.example.demo.service.AuthService
import com.example.demo.service.impl.AuthServiceImpl
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.crypto.password.PasswordEncoder
import spock.lang.Specification

class AuthServiceImplTest extends Specification{

    AuthenticationManager authenticationManager
    JwtUtils jwtUtils
    UserRepository userRepository
    PasswordEncoder encoder
    RoleRepository roleRepository

    AuthService classUnderTest

    def setup(){
        authenticationManager = Mock()
        jwtUtils = Mock()
        userRepository = Mock()
        encoder = Mock()
        roleRepository = Mock()

        classUnderTest = new AuthServiceImpl(authenticationManager, jwtUtils, userRepository, encoder, roleRepository)
    }

    def "authenticateUser should return jwt token"(){
        given:
        def username = "username"
        def password = "password"
        def id = "id"
        def jwt = "testJwt"
        def authorities = List.of( new SimpleGrantedAuthority(EnumRole.ROLE_USER.toString()))
        Authentication authentication = Mock()
        AuthDTO userDTO = Mock()
        def expectedResponse = ResponseEntity.ok(new JwtResponse(
                token: jwt,
                id: id,
                username: username,
                roles: authorities.stream().map(item -> item.getAuthority())
                        .toList()))



        when:
        ResponseEntity actualResponse = classUnderTest.authenticateUser(username, password)

        then:
        1 * userRepository.findByUsername(username) >> Optional.of(new User())
        1 * jwtUtils.generateJwtToken(_) >> jwt
        1 * authenticationManager.authenticate(_) >> authentication
        1 * authentication.getPrincipal() >> userDTO
        1 * userDTO.getAuthorities() >> authorities
        1 * userDTO.id >> id
        1 * userDTO.username >> username
        actualResponse.getBody() == expectedResponse.getBody()
        actualResponse.getStatusCode() == expectedResponse.getStatusCode()
    }

    def "authenticateUser should throw exception while trying to get user and return bad request"(){
        given:
        def username = "test"

        when:
        ResponseEntity response = classUnderTest.authenticateUser(username,'test')

        then:
        1 * userRepository.findByUsername(username) >> Optional.empty()
        response.getStatusCode() == ResponseEntity.badRequest().build().getStatusCode()
    }

    def "registerUser should return ok"(){
        given:
        def signup = SignupDTO.builder().build()

        when:
        ResponseEntity response = classUnderTest.registerUser(signup)

        then:
        1 * roleRepository.findByName(_) >> Optional.of(new Role())
        response.getStatusCode() == ResponseEntity.ok().build().getStatusCode()
    }

    def "registerUser should return bad request username is already taken"(){
        given:
        def signup = SignupDTO.builder().username("test").build()

        when:
        ResponseEntity response = classUnderTest.registerUser(signup)

        then:
        1 * userRepository.existsByUsername(signup.getUsername()) >> true
        response.getStatusCode() == ResponseEntity.badRequest().build().getStatusCode()
        response.getBody() == "Error: Username is already taken!"
    }

    def "registerUser should throw entity not found exception and return bad request"(){
        given:
        def signup = SignupDTO.builder().build()

        when:
        ResponseEntity response = classUnderTest.registerUser(signup)

        then:
        1 * roleRepository.findByName(_) >> Optional.empty()
        response.getStatusCode() == ResponseEntity.badRequest().build().getStatusCode()
    }

    def "logoutUser: user should logout and return ok"(){
        given:
        def authorizationHeader = "Bearer jwt"

        when:
        ResponseEntity response = classUnderTest.logoutUser(authorizationHeader)

        then:
        response.getStatusCode() == ResponseEntity.ok().build().getStatusCode()
    }

}
