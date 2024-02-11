package com.example.demo.repository

import com.example.demo.model.Comment
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface CommentRepository extends MongoRepository<Comment, String>{

    List<Comment> findByPostId(String s)

    void deleteAllByPostId(String postId)
}