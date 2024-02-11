package com.example.demo

import com.example.demo.model.Like
import com.example.demo.model.Post
import com.example.demo.model.User
import com.example.demo.repository.LikeRepository
import com.example.demo.repository.PostRepository
import com.example.demo.repository.UserRepository
import com.example.demo.service.impl.LikeServiceImpl
import org.springframework.http.ResponseEntity
import spock.lang.Specification

class LikeServiceImplTest extends Specification{

    PostRepository postRepository
    UserRepository userRepository
    LikeRepository likeRepository

    LikeServiceImpl classUnderTest

    def setup(){
        postRepository = Mock()
        userRepository = Mock()
        likeRepository = Mock()

        classUnderTest = new LikeServiceImpl(postRepository, userRepository, likeRepository)
    }

    def "likePost: should return status ok"(){
        given:
        def userId = "id"
        def postId = "id"

        when:
        ResponseEntity response = classUnderTest.likePost(userId, postId)

        then:
        1 * postRepository.findById(postId) >> Optional.of(new Post())
        1 * userRepository.findById(userId) >> Optional.of(new User())
        1 * likeRepository.findByUserIdAndPostId(userId, postId) >> Optional.of(new Like())
        1 * likeRepository.save(_) >> new Like(id: "id", userId: userId, postId: postId)
        response.getStatusCode() == ResponseEntity.ok().build().getStatusCode()
    }

    def "likePost: should return status bad request because post is not exist"(){
        given:
        def userId = "id"
        def postId = "id"

        when:
        ResponseEntity response = classUnderTest.likePost(userId, postId)

        then:
        1 * postRepository.findById(postId) >> Optional.empty()
        response.getStatusCode() == ResponseEntity.badRequest().build().getStatusCode()
        }

    def "likePost: should return status bad request because user is not exist"(){
        given:
        def userId = "id"
        def postId = "id"

        when:
        ResponseEntity response = classUnderTest.likePost(userId, postId)

        then:
        1 * postRepository.findById(postId) >> Optional.of(new Post())
        1 * userRepository.findById(userId) >> Optional.empty()
        response.getStatusCode() == ResponseEntity.badRequest().build().getStatusCode()
    }

    def "likePost: should return status bad request because post is already liked by user"(){
        given:
        def userId = "id"
        def postId = "id"

        when:
        ResponseEntity response = classUnderTest.likePost(userId, postId)

        then:
        1 * postRepository.findById(postId) >> Optional.of(new Post())
        1 * userRepository.findById(userId) >> Optional.of(new User())
        1 * likeRepository.findByUserIdAndPostId(userId, postId) >> Optional.empty()
        response.getStatusCode() == ResponseEntity.badRequest().build().getStatusCode()
        response.getBody() == "Post is already liked by user"
    }

    def "unlikePost: should return status ok"(){
        given:
        def userId = "id"
        def postId = "id"

        when:
        ResponseEntity response = classUnderTest.unlikePost(userId, postId)

        then:
        1 * postRepository.findById(postId) >> Optional.of(new Post())
        1 * userRepository.findById(userId) >> Optional.of(new User())
        1 * likeRepository.findByUserIdAndPostId(userId, postId) >> Optional.empty()
        response.getStatusCode() == ResponseEntity.ok().build().getStatusCode()
    }

    def "unlikePost: should return status bad request because post is not exist"(){
        given:
        def userId = "id"
        def postId = "id"

        when:
        ResponseEntity response = classUnderTest.unlikePost(userId, postId)

        then:
        1 * postRepository.findById(postId) >> Optional.empty()
        response.getStatusCode() == ResponseEntity.badRequest().build().getStatusCode()
    }

    def "unlikePost: should return status bad request because user is not exist"(){
        given:
        def userId = "id"
        def postId = "id"

        when:
        ResponseEntity response = classUnderTest.unlikePost(userId, postId)

        then:
        1 * postRepository.findById(postId) >> Optional.of(new Post())
        1 * userRepository.findById(userId) >> Optional.empty()
        response.getStatusCode() == ResponseEntity.badRequest().build().getStatusCode()
    }

    def "unlikePost: should return status bad request because post is not liked by user"(){
        given:
        def userId = "id"
        def postId = "id"

        when:
        ResponseEntity response = classUnderTest.unlikePost(userId, postId)

        then:
        1 * postRepository.findById(postId) >> Optional.of(new Post())
        1 * userRepository.findById(userId) >> Optional.of(new User())
        1 * likeRepository.findByUserIdAndPostId(userId, postId) >> Optional.of(new Like())
        response.getStatusCode() == ResponseEntity.badRequest().build().getStatusCode()
        response.getBody() == "Post is not liked by user"
    }
}
