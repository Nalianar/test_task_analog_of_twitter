package com.example.demo.repository

import com.example.demo.model.User
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository extends MongoRepository<User, String> {

    boolean existsByUsername(String username)

    Optional<User> findByUsername(String username)

    void deleteByUsername(String s)
}