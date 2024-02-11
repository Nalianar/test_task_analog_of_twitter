package com.example.demo.model

import jakarta.validation.constraints.NotBlank
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document


@Document(collection = "users")
class User {

    @Id
    String id
    @NotBlank
    String username
    @NotBlank
    String password

    @DBRef
    Set<Role> roles = new HashSet<>();
}
