package com.example.demo.dto

import groovy.transform.ToString
import groovy.transform.builder.Builder

@Builder
@ToString(includeNames=true)
class CommentDTO {

    String id
    String content
    String userId
    String postId
}
