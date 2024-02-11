package com.example.demo.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document


@Document(collection = "favorite_post")
class FavoritePost {

    @Id
    String id
    String postId
    String userId
}
