package com.challenge3.challenge3.compass.controller;

import com.challenge3.challenge3.compass.model.Comment;
import com.challenge3.challenge3.compass.model.ErrorResponse;
import com.challenge3.challenge3.compass.model.History;
import com.challenge3.challenge3.compass.model.Post;
import com.challenge3.challenge3.compass.model.enums.PostStatusEnum;
import com.challenge3.challenge3.compass.service.Impl.CommentServiceImpl;
import com.challenge3.challenge3.compass.service.Impl.PostServiceImpl;
import com.challenge3.challenge3.compass.service.exceptions.RegraNegocioException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/posts")
public class PostController {

    private PostServiceImpl postService;

    private CommentServiceImpl commentService;

    public PostController(PostServiceImpl postService, CommentServiceImpl commentService){
        this.postService = postService;
        this.commentService = commentService;
    }

    @GetMapping
    public ResponseEntity<?> getAllPosts(){
        List<Post> posts = postService.getAllPosts();
        ErrorResponse errorResponse = new ErrorResponse("Nenhum post encontrado!"
                , new Timestamp(System.currentTimeMillis()),HttpStatus.NOT_FOUND.name());
        if(posts.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(posts);
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getPostById(@PathVariable Long id){
       try{
        Post posts = postService.getPostsById(id);
        return ResponseEntity.status(HttpStatus.OK).body(posts);}
       catch(RegraNegocioException e){
           return ResponseEntity.badRequest().body(e.getMessage());
       }
    }


    @PostMapping("/{id}")
    public ResponseEntity<?> processPost(@PathVariable Long id){
        try {
            Post postCreated = postService.createPost(id);
            return ResponseEntity.status(HttpStatus.OK).body(postCreated);
        }
        catch (RegraNegocioException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



}
