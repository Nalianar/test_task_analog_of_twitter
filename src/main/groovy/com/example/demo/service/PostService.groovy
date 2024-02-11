package com.example.demo.service

import com.example.demo.dto.PostDTO

interface PostService {

    def createPost(PostDTO postDTO)

    def updatePost(String id, String content)

    def deletePost(String s)

    def getPostsByUser(String s)

    def getFeedByUser(String s)

    def makePostFavorite(String userId, String postId)

    def unmakePostFavorite(String s1, String s2)

    def getUserFavoritePost(String s)
}