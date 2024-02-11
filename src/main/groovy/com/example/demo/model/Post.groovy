package com.example.demo.model

import com.example.demo.dto.CommentDTO
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "posts")
class Post {

    @Id
    String id
    String content
    String userId
    Boolean favorite
}
