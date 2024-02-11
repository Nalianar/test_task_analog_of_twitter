package com.example.demo.service.impl

import com.example.demo.dto.CommentDTO
import com.example.demo.dto.LikeDTO
import com.example.demo.dto.PostDTO
import com.example.demo.exception.EntityNotFoundException
import com.example.demo.model.FavoritePost
import com.example.demo.model.Post
import com.example.demo.model.Subscriber
import com.example.demo.repository.*
import com.example.demo.service.PostService
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
@Slf4j
class PostServiceImpl implements PostService {

    @Autowired
    private final UserRepository userRepository

    @Autowired
    private final PostRepository postRepository

    @Autowired
    private final CommentRepository commentRepository

    @Autowired
    private final LikeRepository likeRepository

    @Autowired
    private final SubscriberRepository subscriberRepository

    @Autowired
    private final FavoritePostRepository favoritePostRepository

    PostServiceImpl(UserRepository userRepository, PostRepository postRepository, CommentRepository commentRepository, LikeRepository likeRepository, SubscriberRepository subscriberRepository, FavoritePostRepository favoritePostRepository){
        this.userRepository = userRepository
        this.postRepository = postRepository
        this.commentRepository = commentRepository
        this.likeRepository = likeRepository
        this.subscriberRepository = subscriberRepository
        this.favoritePostRepository = favoritePostRepository
    }

    @Override
    def createPost(PostDTO postDTO) {
        try {
            userRepository.findById(postDTO.userId).orElseThrow({ new EntityNotFoundException("User with id: " + postDTO.userId + " does not exist") })

            Post post = new Post(
                    content: postDTO.content,
                    userId: postDTO.userId
            )

            post = postRepository.save(post)
            return ResponseEntity.ok().body(PostDTO.builder()
                    .id(post.id)
                    .content(post.content)
                    .userId(post.userId)
                    .build())
        } catch (Exception ex){
            log.error(":createPost: cannot create post with this parameters: " + postDTO.toString(), ex)
            return ResponseEntity.badRequest().body("Cannot create post with this parameters: " + postDTO.toString())
        }
    }

    @Override
    def updatePost(String id, String content) {
        try {
            Post post = postRepository.findById(id).orElseThrow({ new EntityNotFoundException("Post with id: " + id + " does not exist")})
            post.content = content
            post = postRepository.save(post)
            return ResponseEntity.ok(PostDTO.builder()
                    .id(post.id)
                    .content(post.content)
                    .userId(post.userId)
                    .build())
        }catch(Exception ex){
            log.error(":updatePost: cannot update post with this parameters: id = " + id, ex)
            return ResponseEntity.badRequest().body("Cannot update post with this parameters: id = " + id)
        }
    }

    @Override
    def deletePost(String id) {
        try {
            postRepository.findById(id).orElseThrow({ new EntityNotFoundException("Post with id: " + id + " does not exist") })
            postRepository.deleteById(id)
            commentRepository.deleteAllByPostId(id)
            likeRepository.deleteAllByPostId(id)
            return ResponseEntity.ok().body("Post was deleted by id = " + id)
        }catch (Exception ex){
            log.error(":deletePost: cannot delete post with this parameters: id = " + id, ex)
            return ResponseEntity.badRequest().body("Cannot delete post with this parameters: id = " + id)
        }
    }

    @Override
    def getPostsByUser(String userId) {
        try {
            userRepository.findById(userId).orElseThrow({ new EntityNotFoundException("User with id: " + userId + " does not exist") })
            List<Post> posts = postRepository.findByUserId(userId)
            List<PostDTO> postsToReturn = posts.stream().map {x-> new PostDTO(
                    id: x.id,
                    content: x.content,
                    userId: x.userId,
                    comments: commentRepository.findByPostId(x.id).stream().map {new CommentDTO(
                            id: it.id,
                            content: it.content,
                            userId: it.userId,
                            postId: it.postId
                    )}.toList(),
                    likes: likeRepository.findByPostId(x.id).stream().map {new LikeDTO(
                            id: it.id,
                            userId: it.userId,
                            postId: it.postId
                    )}.toList()
            )}.toList()
            return ResponseEntity.ok().body(postsToReturn)
        } catch (Exception ex){
            log.error(":getPostsByUser: cannot get posts by user with this parameters: user_id = " + userId, ex)
            return ResponseEntity.badRequest().body("Cannot get posts by user with this parameters: user_id = " + userId)
        }
    }

    @Override
    def getFeedByUser(String userId) {
        try{
            userRepository.findById(userId).orElseThrow({ new EntityNotFoundException("User with id: " + userId + " does not exist") })

            List<Subscriber> subscribers = subscriberRepository.findBySubscriberId(userId)

            List<Post> posts = postRepository.findByUserIdIn(subscribers.stream()
                    .map {it.subscribedId}
                    .toList())


            return ResponseEntity.ok().body(
                    posts.stream().map {new PostDTO(
                            id: it.id,
                            content: it.content,
                            userId: it.userId,
                            comments: commentRepository.findByPostId(it.id).stream().map {new CommentDTO(
                                    id: it.id,
                                    content: it.content,
                                    userId: it.userId,
                                    postId: it.postId
                            )}.toList(),
                            likes: likeRepository.findByPostId(it.id).stream().map {new LikeDTO(
                                    id: it.id,
                                    userId: it.userId,
                                    postId: it.postId
                            )}.toList()
                    )}.toList())
        }catch (Exception ex){
            log.error(":getFeedByUser: cannot get feed by user with this parameters: user_id = " + userId, ex)
            return ResponseEntity.badRequest().body("Cannot get feed by user with this parameters: user_id = " + userId)
        }
    }

    @Override
    def makePostFavorite(String userId, String postId) {
        try{
            userRepository.findById(userId).orElseThrow{ new EntityNotFoundException("User with id: " + userId + " does not exist") }
            postRepository.findById(postId).orElseThrow{ new EntityNotFoundException("Post with id: " + postId + " does not exist") }

            if(!favoritePostRepository.findByUserIdAndPostId(userId, postId).orElse(null)){
                return ResponseEntity.badRequest().body("This post is already favorite")
            }
            if(!favoritePostRepository.findByUserId(userId).orElse(null)){
                return ResponseEntity.badRequest().body("User already have one favorite post")
            }

            FavoritePost favoritePost = new FavoritePost(
                    userId: userId,
                    postId: postId
            )

            favoritePostRepository.save(favoritePost)
            return ResponseEntity.ok().body("Post is now favorite")
        } catch (Exception ex){
            log.error(":makePostFavorite: cannot make post favorite with this parameters: user_id = " + userId + " post_id = " + postId, ex)
            return ResponseEntity.badRequest().body("Cannot make post favorite with this parameters: user_id = " + userId + " post_id = " + postId)
        }
    }

    @Override
    def unmakePostFavorite(String userId, String postId) {
        try{
            userRepository.findById(userId).orElseThrow{ new EntityNotFoundException("User with id: " + userId + " does not exist") }
            postRepository.findById(postId).orElseThrow{ new EntityNotFoundException("Post with id: " + postId + " does not exist") }

            FavoritePost favoritePost = favoritePostRepository.findByUserIdAndPostId(userId, postId).orElseThrow{new EntityNotFoundException("Favorite post with id: " + postId + " does not exist") }

            favoritePostRepository.delete(favoritePost)
            return ResponseEntity.ok().body("Post is now not favorite")
        } catch (Exception ex){
            log.error(":makePostFavorite: cannot make post favorite with this parameters: user_id = " + userId + " post_id = " + postId, ex)
            return ResponseEntity.badRequest().body("Cannot make post favorite with this parameters: user_id = " + userId + " post_id = " + postId)
        }
    }

    @Override
    def getUserFavoritePost(String userId) {
        try {
            userRepository.findById(userId).orElseThrow{ new EntityNotFoundException("User with id: " + userId + " does not exist") }
            FavoritePost favoritePost = favoritePostRepository.findByUserId(userId).orElse(null)
            if(!favoritePost){
                return ResponseEntity.ok().body("User do not have favorite post")
            }
            Post post = postRepository.findById(favoritePost.postId).orElseThrow{ new EntityNotFoundException("Post with id: " + favoritePost.postId + " does not exist") }
            return ResponseEntity.ok().body(PostDTO.builder()
                    .id(post.id)
                    .userId(post.userId)
                    .content(post.content)
                    .comments(commentRepository.findByPostId(post.id).stream().map {new CommentDTO(
                            id: it.id,
                            content: it.content,
                            userId: it.userId,
                            postId: it.postId
                    )}.toList())
                    .likes(likeRepository.findByPostId(post.id).stream().map {new LikeDTO(
                            id: it.id,
                            userId: it.userId,
                            postId: it.postId
                    )}.toList())
                    .build())
        } catch (Exception ex){
            log.error(":getUserFavoritePost: cannot get users favorite post with this parameters: user_id = " + userId, ex)
            return ResponseEntity.badRequest().body("Cannot get users favorite post with this parameters: user_id = " + userId)

        }
    }
}
