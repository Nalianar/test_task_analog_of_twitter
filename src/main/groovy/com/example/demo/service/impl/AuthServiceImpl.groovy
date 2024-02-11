package com.example.demo.service.impl

import com.example.demo.dto.AuthDTO
import com.example.demo.dto.SignupDTO
import com.example.demo.dto.UserDTO
import com.example.demo.exception.EntityNotFoundException
import com.example.demo.model.EnumRole
import com.example.demo.model.Role
import com.example.demo.model.User
import com.example.demo.repository.RoleRepository
import com.example.demo.repository.UserRepository
import com.example.demo.security.jwt.JwtUtils
import com.example.demo.security.payload.JwtResponse
import com.example.demo.service.AuthService
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
@Slf4j
class AuthServiceImpl implements AuthService {

    @Autowired
    AuthenticationManager authenticationManager

    @Autowired
    JwtUtils jwtUtils

    @Autowired
    UserRepository userRepository

    @Autowired
    PasswordEncoder encoder

    @Autowired
    RoleRepository roleRepository

    AuthServiceImpl(AuthenticationManager authenticationManager, JwtUtils jwtUtils, UserRepository userRepository, PasswordEncoder encoder, RoleRepository roleRepository){
        this.authenticationManager = authenticationManager
        this.jwtUtils = jwtUtils
        this.userRepository = userRepository
        this.encoder = encoder
        this.roleRepository = roleRepository
    }


    @Override
    def authenticateUser(String username, String password) {
        try {
            userRepository.findByUsername(username).orElseThrow({ new EntityNotFoundException("User with username " + username + " does not exist") })

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password))

            SecurityContextHolder.getContext().setAuthentication(authentication)
            String jwt = jwtUtils.generateJwtToken(authentication)

            AuthDTO userDetails = (AuthDTO) authentication.getPrincipal()
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(item -> item.getAuthority())
                    .toList()

            return ResponseEntity.ok(new JwtResponse(
                    token: jwt,
                    id: userDetails.id,
                    username: userDetails.username,
                    roles: roles))
        } catch (Exception ex){
            log.error(":authenticateUser: cannot authenticate user with this parameters: user_name = " + username, ex)
            return ResponseEntity.badRequest().body("Cannot authenticate user with this parameters: user_name = " + username)

        }
    }

    @Override
    def registerUser(SignupDTO signupDTO) {
        try {
            if (userRepository.existsByUsername(signupDTO.getUsername())) {
                return ResponseEntity
                        .badRequest()
                        .body("Error: Username is already taken!");
            }
            User user = new User(
                    username: signupDTO.username,
                    password: encoder.encode(signupDTO.password));

            Set<String> strRoles = signupDTO.getRoles();
            Set<Role> roles = new HashSet<>();

            if (strRoles == null) {
                Role userRole = roleRepository.findByName(EnumRole.ROLE_USER)
                        .orElseThrow(() -> new EntityNotFoundException("Error: Role is not found."));
                roles.add(userRole);
            } else {
                strRoles.forEach(role -> {
                    switch (role) {
                        case "admin":
                            Role adminRole = roleRepository.findByName(EnumRole.ROLE_ADMIN)
                                    .orElseThrow(() -> new EntityNotFoundException("Error: Role is not found."));
                            roles.add(adminRole);
                            break;
                        default:
                            Role userRole = roleRepository.findByName(EnumRole.ROLE_USER)
                                    .orElseThrow(() -> new EntityNotFoundException("Error: Role is not found."));
                            roles.add(userRole);
                    }
                });
            }

            user.setRoles(roles);
            userRepository.save(user);

            return ResponseEntity.ok("User registered successfully!");
        } catch (Exception ex){
            log.error(":registerUser: cannot signup user with this parameters: " + signupDTO, ex)
            return ResponseEntity.badRequest().body("Cannot signup user with this parameters: " + signupDTO)
        }
    }

    @Override
    def logoutUser(String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "")
        jwtUtils.invalidateToken(token)
        return ResponseEntity.ok().body("User logout successfully")
    }
}
