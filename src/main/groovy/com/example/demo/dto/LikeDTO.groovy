package com.example.demo.dto

import groovy.transform.builder.Builder

@Builder
class LikeDTO {

    String id
    String userId
    String postId
}
