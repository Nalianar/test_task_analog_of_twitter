package com.example.demo.controller

import com.example.demo.dto.PostDTO
import com.example.demo.service.PostService
import jakarta.validation.constraints.NotBlank
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/posts")
class PostController {

    @Autowired
    private PostService postService

    @PostMapping("/create")
    def createPost(@RequestBody @NotBlank PostDTO postDTO) {
        return postService.createPost(postDTO)
    }

    @PutMapping("/update/{id}")
    def updatePost(@PathVariable @NotBlank String id, @RequestBody @NotBlank PostDTO postDTO) {
        return postService.updatePost(id, postDTO.getContent())
    }

    @DeleteMapping("/{id}")
    def deletePost(@PathVariable @NotBlank String id) {
        return postService.deletePost(id)
    }

    @GetMapping("/user/{userId}")
    def getPostsByUser(@PathVariable @NotBlank String userId) {
        return postService.getPostsByUser(userId)
    }

    @GetMapping("/feed/{userId}")
    def getFeedByUser(@PathVariable @NotBlank String userId) {
        return postService.getFeedByUser(userId)
    }

    @PostMapping("/favorite")
    def makeUserFavoritePost(@PathVariable @NotBlank String userId, @PathVariable @NotBlank String postId){
        return postService.makePostFavorite(userId, postId)
    }

    @DeleteMapping("/unfavorite")
    def unmakeUserFavoritePost(@PathVariable @NotBlank String userId, @PathVariable @NotBlank String postId){
        return  postService.unmakePostFavorite(userId, postId)
    }

    @GetMapping("/favorite")
    def getUserFavoritePost(@PathVariable @NotBlank String userId){
        return postService.getUserFavoritePost(userId)
    }
}
