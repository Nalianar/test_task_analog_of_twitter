package com.example.demo.service.impl

import com.example.demo.dto.UserDTO
import com.example.demo.exception.EntityNotFoundException
import com.example.demo.model.User
import com.example.demo.repository.UserRepository
import com.example.demo.service.UserService
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpServerErrorException.InternalServerError

@Service
@Slf4j
class UserServiceImpl implements UserService {

    @Autowired
    private final UserRepository userRepository

    @Autowired
    private final PasswordEncoder passwordEncoder

    UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository
        this.passwordEncoder = passwordEncoder
    }

    @Override
    def updateUserById(String id, String username, String password) {
        try {
            User user = userRepository.findById(id).orElseThrow({new EntityNotFoundException("User with id: " + id + " does not exist")})
            if (user.getUsername() == username && userRepository.existsByUsername(username)) {
                return ResponseEntity.badRequest().body("This username is taken")
            }
            user.username = username
            user.password = passwordEncoder.encode(password)
            user = userRepository.save(user)

            return ResponseEntity.ok(UserDTO.builder()
                    .id(user.id)
                    .username(user.username)
                    .password(user.password)
                    .build())
        } catch(Exception ex){
            log.error(":updateUserById: cannot update user with this parameters: id = " + id +" username = "+ username, ex)
            return ResponseEntity.badRequest().body("Cannot update user with this parameters: id = " + id +" username = "+ username)
        }
    }

    @Override
    def deleteUserById(String id) {
        try {
            userRepository.findById(id).orElseThrow({ new EntityNotFoundException("User with id: " + id + " does not exist") })
            userRepository.deleteById(id)
            return ResponseEntity.ok().body("User was deleted")
        } catch (Exception ex){
            log.error(":deleteUserById: cannot delete user with this parameters: id = " + id , ex)
            return ResponseEntity.badRequest().body("Cannot delete user with this parameters: id = " + id)
        }
    }

    @Override
    def deleteUserByUsername(String username) {
        try {
            userRepository.findByUsername(username).orElseThrow({ new EntityNotFoundException("User with username: " + username + " does not exist") })
            userRepository.deleteByUsername(username)
            return ResponseEntity.ok().body("User was deleted")
        } catch(Exception ex){
            log.error(":deleteUserByUsername: cannot delete user with this parameters: username = " + username , ex)
            return ResponseEntity.badRequest().body("Cannot delete user with this parameters: username = " + username)
        }
    }
}
