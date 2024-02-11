package com.example.demo.repository

import com.example.demo.model.Post
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface PostRepository extends MongoRepository<Post, String> {
    List<Post> findByUserIdIn(List<String> strings)

    List<Post> findByUserId(String s)
}
