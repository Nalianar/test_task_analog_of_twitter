package com.example.demo.repository

import com.example.demo.model.Like
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface LikeRepository extends MongoRepository<Like, String>{

    Optional<Like> findByUserIdAndPostId(String userId, String postId)

    List<Like> findByPostId(String postId)

    void deleteAllByPostId(String PostId)


}