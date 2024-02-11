package com.example.demo

import com.example.demo.dto.CommentDTO
import com.example.demo.model.Comment
import com.example.demo.model.Post
import com.example.demo.repository.CommentRepository
import com.example.demo.repository.PostRepository
import com.example.demo.repository.UserRepository
import com.example.demo.service.impl.CommentServiceImpl
import org.springframework.http.ResponseEntity
import spock.lang.Specification

class CommentServiceImplTest extends Specification{

    UserRepository userRepository
    PostRepository postRepository
    CommentRepository commentRepository

    CommentServiceImpl classUnderTest

    def setup(){
        userRepository = Mock()
        postRepository = Mock()
        commentRepository = Mock()

        classUnderTest = new CommentServiceImpl(userRepository, postRepository, commentRepository)
    }

    def "createComment: should return status ok"(){
        given:
        def id = "id"
        def comment = CommentDTO.builder().postId(id).userId(id).build()

        when:
        ResponseEntity response = classUnderTest.createComment(comment)

        then:
        1 * userRepository.existsById(_) >> true
        1 * postRepository.findById(_) >> Optional.of(new Post())
        1 * commentRepository.save(_) >> new Comment(id: "id", userId: "userId", postId: "postId", content: "content")
        response.getStatusCode() == ResponseEntity.ok().build().getStatusCode()
    }

    def "createComment: should return status bad request because post do not exist"(){
        given:
        def id = "id"
        def comment = CommentDTO.builder().postId(id).userId(id).build()

        when:
        ResponseEntity response = classUnderTest.createComment(comment)

        then:
        1 * userRepository.existsById(_) >> true
        1 * postRepository.findById(_) >> Optional.empty()
        response.getStatusCode() == ResponseEntity.badRequest().build().getStatusCode()
    }

    def "createComment: should return status bad request user with id do not exist"(){
        given:
        def id = "id"
        def comment = CommentDTO.builder().postId(id).userId(id).build()

        when:
        ResponseEntity response = classUnderTest.createComment(comment)

        then:
        1 * userRepository.existsById(comment.userId) >> false
        response.getStatusCode() == ResponseEntity.badRequest().build().getStatusCode()
        response.getBody() == ResponseEntity.badRequest().body("User with id:" + comment.userId + " does not exists").getBody()
    }

    def "getCommentsByPostId: should return status ok"(){
        given:
        def id = "id"

        when:
        ResponseEntity response = classUnderTest.getCommentsByPostId(id)

        then:
        1 * postRepository.findById(id) >> Optional.of(new Post())
        1 * commentRepository.findByPostId(id) >> List.of(new Comment())
        response.getStatusCode() == ResponseEntity.ok().build().getStatusCode()
    }

    def "getCommentsByPostId: should return status bad request"(){
        given:
        def id = "id"

        when:
        ResponseEntity response = classUnderTest.getCommentsByPostId(id)

        then:
        1 * postRepository.findById(id) >> Optional.empty()
        response.getStatusCode() == ResponseEntity.badRequest().build().getStatusCode()
    }

}
