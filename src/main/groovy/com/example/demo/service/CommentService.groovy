package com.example.demo.service

import com.example.demo.dto.CommentDTO

interface CommentService {

    def createComment(CommentDTO commentDTO)

    def getCommentsByPostId(String postId)
}
