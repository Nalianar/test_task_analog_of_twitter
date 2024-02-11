package com.example.demo.repository

import com.example.demo.model.EnumRole
import com.example.demo.model.Role
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface RoleRepository extends MongoRepository<Role, String> {

    Optional<Role> findByName(EnumRole enumRole)
}