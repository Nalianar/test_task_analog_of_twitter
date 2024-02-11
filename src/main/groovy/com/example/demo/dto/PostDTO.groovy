package com.example.demo.dto

import groovy.transform.ToString
import groovy.transform.builder.Builder

@Builder
@ToString(includeNames=true)
class PostDTO {

    String id
    String content
    String userId
    List<CommentDTO> comments
    List<LikeDTO> likes
}
