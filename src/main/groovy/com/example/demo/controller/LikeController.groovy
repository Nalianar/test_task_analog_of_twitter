package com.example.demo.controller

import com.example.demo.service.LikeService
import jakarta.validation.constraints.NotBlank
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/likes")
class LikeController {

    @Autowired
    private LikeService likeService


    @PostMapping("/like")
    def likePost(@RequestParam @NotBlank String userId, @RequestParam @NotBlank String postId){
        return likeService.likePost(userId, postId)
    }

    @DeleteMapping("/unlike")
    def unlikePost(@RequestParam @NotBlank String userId, @RequestParam @NotBlank String postId){
        return likeService.unlikePost(userId, postId)
    }

}
