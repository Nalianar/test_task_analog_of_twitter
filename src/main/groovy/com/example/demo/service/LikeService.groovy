package com.example.demo.service

interface LikeService {

    def likePost(String userId, String postId)

    def unlikePost(String userId, String postId)

}