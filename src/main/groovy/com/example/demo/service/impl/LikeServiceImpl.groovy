package com.example.demo.service.impl

import com.example.demo.dto.LikeDTO
import com.example.demo.exception.EntityNotFoundException
import com.example.demo.model.Like
import com.example.demo.repository.LikeRepository
import com.example.demo.repository.PostRepository
import com.example.demo.repository.UserRepository
import com.example.demo.service.LikeService
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service


@Service
@Slf4j
class LikeServiceImpl implements LikeService {

    @Autowired
    private final PostRepository postRepository

    @Autowired
    private final UserRepository userRepository

    @Autowired
    private final LikeRepository likeRepository


    LikeServiceImpl(PostRepository postRepository, UserRepository userRepository, LikeRepository likeRepository){
        this.postRepository = postRepository
        this.userRepository = userRepository
        this.likeRepository = likeRepository
    }


    @Override
    def likePost(String userId, String postId) {
        try {
            postRepository.findById(postId).orElseThrow ({ new EntityNotFoundException("Post with id: " + postId + " does not exist") })
            userRepository.findById(userId).orElseThrow({ new EntityNotFoundException("User with id: " + userId + " does not exist") })

            Like like = likeRepository.findByUserIdAndPostId(userId, postId).orElse(null)

            if(!like){
                return ResponseEntity.badRequest().body("Post is already liked by user")
            }

            like = new Like(
                    userId: userId,
                    postId: postId)
            like = likeRepository.save(like)

            return ResponseEntity.ok().body(LikeDTO.builder()
                    .id(like.id)
                    .userId(userId)
                    .postId(postId)
                    .build())
        } catch (Exception ex){
            log.error(":likePost: cannot like post with this parameters: user_id = " + userId + " post_id = " + postId, ex)
            return ResponseEntity.badRequest().body("Cannot like post with this parameters: user_id = " + userId + " post_id = " + postId)
        }
    }

    @Override
    def unlikePost(String userId, String postId) {
        try {
            postRepository.findById(postId).orElseThrow ({ new EntityNotFoundException("Post with id: " + postId + " does not exist") })
            userRepository.findById(userId).orElseThrow({ new EntityNotFoundException("User with id: " + userId + " does not exist") })

            Like like = likeRepository.findByUserIdAndPostId(userId, postId).orElse (null)

            if(like){
                return ResponseEntity.badRequest().body("Post is not liked by user")
            }

            likeRepository.delete(like)

            return ResponseEntity.ok().body("Post is unliked")
        } catch (Exception ex){
            log.error(":unlikePost: cannot unlike post with this parameters: user_id = " + userId + " post_id = " + postId, ex)
            return ResponseEntity.badRequest().body("Cannot unlike post with this parameters: user_id = " + userId + " post_id = " + postId)
        }
    }
}
