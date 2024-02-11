package com.example.demo

import com.example.demo.dto.PostDTO
import com.example.demo.model.FavoritePost
import com.example.demo.model.Post
import com.example.demo.model.User
import com.example.demo.repository.CommentRepository
import com.example.demo.repository.FavoritePostRepository
import com.example.demo.repository.LikeRepository
import com.example.demo.repository.PostRepository
import com.example.demo.repository.SubscriberRepository
import com.example.demo.repository.UserRepository
import com.example.demo.service.impl.PostServiceImpl
import org.springframework.http.ResponseEntity
import spock.lang.Specification

class PostServiceImplTest extends Specification{
    UserRepository userRepository
    PostRepository postRepository
    CommentRepository commentRepository
    LikeRepository likeRepository
    SubscriberRepository subscriberRepository
    FavoritePostRepository favoritePostRepository

    PostServiceImpl classUnderTest

    def setup(){
        userRepository = Mock()
        postRepository = Mock()
        commentRepository = Mock()
        likeRepository = Mock()
        subscriberRepository = Mock()
        favoritePostRepository = Mock()

        classUnderTest = new PostServiceImpl(userRepository, postRepository, commentRepository, likeRepository, subscriberRepository, favoritePostRepository)
    }

    def "createPost: should return status ok"(){
        given:
        def post = PostDTO.builder().build()

        when:
        ResponseEntity response = classUnderTest.createPost(post)

        then:
        1 * userRepository.findById(_) >> Optional.of(new User())
        1 * postRepository.save(_) >> new Post(id: "id", content: "content", userId: "userId")
        response.getStatusCode() == ResponseEntity.ok().build().getStatusCode()
    }

    def "createPost: should return status bad request because user is not exist"(){
        given:
        def post = PostDTO.builder().build()

        when:
        ResponseEntity response = classUnderTest.createPost(post)

        then:
        1 * userRepository.findById(_) >> Optional.empty()
        response.getStatusCode() == ResponseEntity.badRequest().build().getStatusCode()
    }

    def "updatePost: should return ok"(){
        given:
        def id = "id"
        def content = "content"

        when:
        ResponseEntity response = classUnderTest.updatePost(id, content)

        then:
        1 * postRepository.findById(_) >> Optional.of(new Post())
        1 * postRepository.save(_) >> new Post(id: "id", content: "content1", userId: "userId")
        response.getStatusCode() == ResponseEntity.ok().build().getStatusCode()

    }

    def "updatePost: should return bad request because post is not exists"(){
        given:
        def id = "id"
        def content = "content"

        when:
        ResponseEntity response = classUnderTest.updatePost(id, content)

        then:
        1 * postRepository.findById(_) >> Optional.empty()
        response.getStatusCode() == ResponseEntity.badRequest().build().getStatusCode()

    }

    def "deletePost: should return ok"(){
        given:
        def id = "id"

        when:
        ResponseEntity response = classUnderTest.deletePost(id)

        then:
        1 * postRepository.findById(_) >> Optional.of(new Post())
        response.getStatusCode() == ResponseEntity.ok().build().getStatusCode()

    }

    def "deletePost: should return bad request because post is not exists"(){
        given:
        def id = "id"

        when:
        ResponseEntity response = classUnderTest.deletePost(id)

        then:
        1 * postRepository.findById(_) >> Optional.empty()
        response.getStatusCode() == ResponseEntity.badRequest().build().getStatusCode()

    }

    def "getPostsByUser: should return ok"(){
        given:
        def id = "id"

        when:
        ResponseEntity response = classUnderTest.getPostsByUser(id)

        then:
        1 * userRepository.findById(_) >> Optional.of(new User())
        response.getStatusCode() == ResponseEntity.ok().build().getStatusCode()

    }

    def "getPostsByUser: should return bad request because user is not exists"(){
        given:
        def id = "id"

        when:
        ResponseEntity response = classUnderTest.getPostsByUser(id)

        then:
        1 * userRepository.findById(_) >> Optional.empty()
        response.getStatusCode() == ResponseEntity.badRequest().build().getStatusCode()

    }

    def "getFeedByUser: should return ok"(){
        given:
        def id = "id"

        when:
        ResponseEntity response = classUnderTest.getFeedByUser(id)

        then:
        1 * userRepository.findById(_) >> Optional.of(new User())
        response.getStatusCode() == ResponseEntity.ok().build().getStatusCode()

    }

    def "getFeedByUser: should return bad request because user is not exists"(){
        given:
        def id = "id"

        when:
        ResponseEntity response = classUnderTest.getFeedByUser(id)

        then:
        1 * userRepository.findById(_) >> Optional.empty()
        response.getStatusCode() == ResponseEntity.badRequest().build().getStatusCode()

    }

    def "makePostFavorite: should return ok"(){
        given:
        def userId = "id"
        def postId = "id"

        when:
        ResponseEntity response = classUnderTest.makePostFavorite(userId, postId)

        then:
        1 * userRepository.findById(_) >> Optional.of(new User())
        1 * postRepository.findById(_) >> Optional.of(new Post())
        1 * favoritePostRepository.findByUserIdAndPostId(_, _) >> Optional.of(new FavoritePost())
        1 * favoritePostRepository.findByUserId(_) >> Optional.of(new FavoritePost())
        response.getStatusCode() == ResponseEntity.ok().build().getStatusCode()
    }

    def "makePostFavorite: should return bad request because user is not exists"(){
        given:
        def userId = "id"
        def postId = "id"

        when:
        ResponseEntity response = classUnderTest.makePostFavorite(userId, postId)

        then:
        1 * userRepository.findById(_) >> Optional.empty()
        response.getStatusCode() == ResponseEntity.badRequest().build().getStatusCode()
    }


    def "makePostFavorite: should return bad request because post is not exists"(){
        given:
        def userId = "id"
        def postId = "id"

        when:
        ResponseEntity response = classUnderTest.makePostFavorite(userId, postId)

        then:

        1 * userRepository.findById(_) >> Optional.of(new User())
        1 * postRepository.findById(_) >> Optional.empty()
        response.getStatusCode() == ResponseEntity.badRequest().build().getStatusCode()
    }

    def "makePostFavorite: should return bad request because post is already favorite"(){
        given:
        def userId = "id"
        def postId = "id"

        when:
        ResponseEntity response = classUnderTest.makePostFavorite(userId, postId)

        then:
        1 * userRepository.findById(_) >> Optional.of(new User())
        1 * postRepository.findById(_) >> Optional.of(new Post())
        1 * favoritePostRepository.findByUserIdAndPostId(_, _) >> Optional.empty()
        response.getStatusCode() == ResponseEntity.badRequest().build().getStatusCode()
        response.getBody() == "This post is already favorite"
    }

    def "makePostFavorite: should return bad request because user already have favorite post"(){
        given:
        def userId = "id"
        def postId = "id"

        when:
        ResponseEntity response = classUnderTest.makePostFavorite(userId, postId)

        then:
        1 * userRepository.findById(_) >> Optional.of(new User())
        1 * postRepository.findById(_) >> Optional.of(new Post())
        1 * favoritePostRepository.findByUserIdAndPostId(_, _) >> Optional.of(new FavoritePost())
        1 * favoritePostRepository.findByUserId(_) >> Optional.empty()
        response.getStatusCode() == ResponseEntity.badRequest().build().getStatusCode()
        response.getBody() == "User already have one favorite post"
    }


    def "unmakePostFavorite: should return ok"(){
        given:
        def userId = "id"
        def postId = "id"

        when:
        ResponseEntity response = classUnderTest.unmakePostFavorite(userId, postId)

        then:
        1 * userRepository.findById(_) >> Optional.of(new User())
        1 * postRepository.findById(_) >> Optional.of(new Post())
        1 * favoritePostRepository.findByUserIdAndPostId(_, _) >> Optional.of(new FavoritePost())
        response.getStatusCode() == ResponseEntity.ok().build().getStatusCode()
    }


    def "unmakePostFavorite: should return bad request because user is not exists"(){
        given:
        def userId = "id"
        def postId = "id"

        when:
        ResponseEntity response = classUnderTest.unmakePostFavorite(userId, postId)

        then:
        1 * userRepository.findById(_) >> Optional.empty()
        response.getStatusCode() == ResponseEntity.badRequest().build().getStatusCode()
    }


    def "unmakePostFavorite: should return bad request because post is not exists"(){
        given:
        def userId = "id"
        def postId = "id"

        when:
        ResponseEntity response = classUnderTest.unmakePostFavorite(userId, postId)

        then:

        1 * userRepository.findById(_) >> Optional.of(new User())
        1 * postRepository.findById(_) >> Optional.empty()
        response.getStatusCode() == ResponseEntity.badRequest().build().getStatusCode()
    }

    def "getUserFavoritePost: should return ok"(){
        given:
        def userId = "id"

        when:
        ResponseEntity response = classUnderTest.getUserFavoritePost(userId)

        then:
        1 * userRepository.findById(_) >> Optional.of(new User())
        1 * favoritePostRepository.findByUserId(_) >> Optional.of(new FavoritePost())
        1 * postRepository.findById(_) >> Optional.of(new Post(id:"id", userId: userId, content: "content"))
        response.getStatusCode() == ResponseEntity.ok().build().getStatusCode()
    }


    def "getUserFavoritePost: should return bad request because user is not exists"(){
        given:
        def userId = "id"

        when:
        ResponseEntity response = classUnderTest.getUserFavoritePost(userId)

        then:
        1 * userRepository.findById(_) >> Optional.empty()
        response.getStatusCode() == ResponseEntity.badRequest().build().getStatusCode()
    }


    def "getUserFavoritePost: should return ok because user do not have favorite post"(){
        given:
        def userId = "id"
        def postId = "id"

        when:
        ResponseEntity response = classUnderTest.getUserFavoritePost(userId)

        then:

        1 * userRepository.findById(_) >> Optional.of(new User())
        1 * favoritePostRepository.findByUserId(_) >> Optional.empty()
        response.getStatusCode() == ResponseEntity.ok().build().getStatusCode()
        response.getBody() == "User do not have favorite post"
    }

    def "getUserFavoritePost: should return bad request because post that user have as favorite do not exist"(){
        given:
        def userId = "id"

        when:
        ResponseEntity response = classUnderTest.getUserFavoritePost(userId)

        then:
        1 * userRepository.findById(_) >> Optional.of(new User())
        1 * favoritePostRepository.findByUserId(_) >> Optional.of(new FavoritePost())
        1 * postRepository.findById(_) >> Optional.empty()
        response.getStatusCode() == ResponseEntity.badRequest().build().getStatusCode()
    }

}
