package com.challenge3.challenge3.compass.controller;

import com.challenge3.challenge3.compass.model.Comment;
import com.challenge3.challenge3.compass.model.ErrorResponse;
import com.challenge3.challenge3.compass.model.History;
import com.challenge3.challenge3.compass.model.Post;
import com.challenge3.challenge3.compass.model.enums.PostStatusEnum;
import com.challenge3.challenge3.compass.service.Impl.PostServiceImpl;
import com.challenge3.challenge3.compass.service.exceptions.RegraNegocioException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/posts")
public class PostController {

    private PostServiceImpl postService;


    public PostController(PostServiceImpl postService){
        this.postService = postService;


    }

    @GetMapping
    public ResponseEntity<?> getAllPosts
            (@RequestParam (value = "pageNo", defaultValue = "0", required = false) int pageNo,
    @RequestParam(value = "pageSize", defaultValue = "10", required = false)int pageSize){
        List<Post> posts = postService.getAllPosts(pageNo, pageSize);
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
           Optional<Post> postToFind = postService.getPostsById(id);
           if(postToFind.isEmpty()){
               throw new RegraNegocioException("Post ainda não foi criado!");
           }
           return ResponseEntity.status(HttpStatus.OK).body(postToFind);
       }
       catch(RegraNegocioException e){
           return ResponseEntity.badRequest().body(e.getMessage());
       }
    }


    @PostMapping("/{id}")
    public ResponseEntity<?> processPost(@PathVariable Long id){
        try {
            Optional<Post> postToFind = postService.getPostsById(id);
            if(postToFind.isPresent()) {
                Post post = postToFind.get();
                List<History> historyList = post.getHistory();
                if (!historyList.isEmpty()) {
                    History history = historyList.get(historyList.size() - 1);
                    PostStatusEnum status = history.getStatus();
                    switch (status) {
                        case CREATED:
                            postService.findPost(id);
                            return ResponseEntity.status(HttpStatus.OK).body(post);
                        case POST_FIND:
                            postService.validatePostFound(id);
                            return ResponseEntity.status(HttpStatus.OK).body(post);
                        case POST_OK:
                            postService.findValidComments(id);
                            return ResponseEntity.status(HttpStatus.OK).body(post);
                        case COMMENTS_FIND:
                            postService.setCommentsIntoPostAndValidate(id);
                            return ResponseEntity.status(HttpStatus.OK).body(post);
                        case COMMENTS_OK:
                            postService.enablePost(id);
                            return ResponseEntity.status(HttpStatus.OK).body(post);
                        case ENABLED:
                            return ResponseEntity.status(HttpStatus.OK).body("O post está habilitado. Você deve " +
                                    "reprocessá-lo no método PUT, desabilitá-lo no método DELETE ou vê-lo no método GET!");
                        case DISABLED:
                            return ResponseEntity.status(HttpStatus.OK).body("O post está desabilitado. Você deve " +
                                    "reprocessá-lo no método PUT ou vê-lo no método GET!");
                        case FAILED:
                            postService.disablePost(id);
                            return ResponseEntity.status(HttpStatus.OK).body(post);
                        case UPDATING:
                            post.setHistory(new ArrayList<>());
                            post.setComments(new ArrayList<>());
                            postService.reprocessPost(id);
                            ResponseEntity.status(HttpStatus.OK).body(post);;
                    }
                }
            }
            Post postCreated = postService.createPost(id);
            return ResponseEntity.status(HttpStatus.CREATED).body(postCreated);
        }
        catch (RegraNegocioException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> disablePost(@PathVariable Long id) {
        try {
            Optional<Post> postToFind = postService.getPostsById(id);
            if (postToFind.isPresent()) {
                Post post = postToFind.get();
                List<History> historyList = post.getHistory();
                    History history = historyList.get(historyList.size() - 1);
                    PostStatusEnum status = history.getStatus();
                    if (status == PostStatusEnum.ENABLED || status == PostStatusEnum.FAILED) {
                        Post postDisabled = postService.disablePost(id);
                        return ResponseEntity.status(HttpStatus.OK).body("O post está desabilitado. Para vê-lo" +
                                " use o método GET e para reprocessá-lo o método PUT!");
                }
            }
            throw new RegraNegocioException("O post não pode ser desabilitado pois ainda não foi habilitado!");
        }
        catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> reprocessPost(@PathVariable Long id){
        try {
            Optional<Post> postToFind = postService.getPostsById(id);
            if (postToFind.isPresent()) {
                Post post = postToFind.get();
                List<History> historyList = post.getHistory();
                History history = historyList.get(historyList.size() - 1);
                PostStatusEnum status = history.getStatus();
                if (status == PostStatusEnum.ENABLED || status == PostStatusEnum.DISABLED) {
                    Post postToPrepare = postService.prepareToReprocessPost(id);
                    return ResponseEntity.status(HttpStatus.OK).body(postToPrepare);
                }
                else if (status == PostStatusEnum.UPDATING) {
                    post.setHistory(new ArrayList<>());
                    post.setComments(new ArrayList<>());
                    postService.reprocessPost(id);
                    return ResponseEntity.status(HttpStatus.OK).body(post);
                }
            }
            throw new RegraNegocioException("O post não pode ser reprocessado pois não está no status adequado." +
                    "Use o método POST para processá-lo, GET para vê-lo e DELETE para desabilitá-lo!");
        }
        catch (RegraNegocioException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
