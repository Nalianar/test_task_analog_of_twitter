package com.example.demo.controller

import com.example.demo.dto.CommentDTO
import com.example.demo.service.CommentService
import jakarta.validation.constraints.NotBlank
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/comments")
class CommentController {

    @Autowired
    private CommentService commentService


    @PostMapping("/create")
    def createComment(@RequestBody @NotBlank CommentDTO commentDTO) {
        return commentService.createComment(commentDTO)
    }

    @GetMapping("/post/{postId}")
    def getCommentsByPostId(@PathVariable @NotBlank String postId) {
        return commentService.getCommentsByPostId(postId)
    }
}
