package com.example.demo.repository

import com.example.demo.model.FavoritePost
import org.springframework.data.mongodb.repository.MongoRepository

interface FavoritePostRepository extends MongoRepository<FavoritePost, String>{

    Optional<FavoritePost> findByUserIdAndPostId(String s1, String s2)

    Optional<FavoritePost> findByUserId(String s)
}