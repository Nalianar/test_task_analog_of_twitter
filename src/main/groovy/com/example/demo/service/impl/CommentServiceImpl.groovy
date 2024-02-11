package com.example.demo.service.impl

import com.example.demo.dto.CommentDTO
import com.example.demo.exception.EntityNotFoundException
import com.example.demo.model.Comment
import com.example.demo.model.Post
import com.example.demo.repository.CommentRepository
import com.example.demo.repository.PostRepository
import com.example.demo.repository.UserRepository
import com.example.demo.service.CommentService
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
@Slf4j
class CommentServiceImpl implements CommentService {

    @Autowired
    private final UserRepository userRepository

    @Autowired
    private final PostRepository postRepository

    @Autowired
    private final CommentRepository commentRepository

    CommentServiceImpl(UserRepository userRepository, PostRepository postRepository, CommentRepository commentRepository){
        this.userRepository = userRepository
        this.postRepository = postRepository
        this.commentRepository = commentRepository
    }

    @Override
    def createComment(CommentDTO commentDTO) {
        try {
            if (!userRepository.existsById(commentDTO.userId)) {
                return ResponseEntity.badRequest().body("User with id:" + commentDTO.userId + " does not exists")
            }
            postRepository.findById(commentDTO.postId).orElseThrow({ new EntityNotFoundException("Post with id: " + commentDTO.postId + " does not exist")})

            Comment comment = new Comment()
            comment.content = commentDTO.content
            comment.userId = commentDTO.userId
            comment.postId = commentDTO.postId
            comment = commentRepository.save(comment)
            return ResponseEntity.ok()
                    .body(CommentDTO.builder()
                            .id(comment.id)
                            .content(comment.content)
                            .userId(comment.userId)
                            .postId(comment.postId)
                            .build())
        } catch(Exception ex){
            log.error(":createComment: cannot create comment on post with this parameters: " + commentDTO.toString(), ex)
            return ResponseEntity.badRequest().body("Cannot create comment on post with this parameters: " + commentDTO.toString())
        }
    }

    @Override
    def getCommentsByPostId(String postId) {
        try {
            postRepository.findById(postId).orElseThrow({ new EntityNotFoundException("Post with id: " + postId + " does not exist")})
            List<Comment> comments = commentRepository.findByPostId(postId)
            return ResponseEntity.ok().body(comments.isEmpty() ? "" :
                    comments.stream().map(x -> new CommentDTO(
                            id: x.id,
                            content: x.content,
                            userId: x.userId,
                            postId: x.postId
                    )).toList())
        } catch (Exception ex){
            log.error(":getCommentsByPostId: cannot get comments on post with this parameters: post_id = " + postId, ex)
            return ResponseEntity.badRequest().body("Cannot get comments on post with this parameters: post_id = " + postId)
        }
    }
}
