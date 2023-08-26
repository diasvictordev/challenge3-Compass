package com.challenge3.challenge3.compass.service.Impl;

import com.challenge3.challenge3.compass.client.PostClient;
import com.challenge3.challenge3.compass.model.Comment;
import com.challenge3.challenge3.compass.model.History;
import com.challenge3.challenge3.compass.model.Post;
import com.challenge3.challenge3.compass.model.enums.PostStatusEnum;
import com.challenge3.challenge3.compass.repository.CommentRepository;
import com.challenge3.challenge3.compass.repository.PostRepository;
import com.challenge3.challenge3.compass.service.PostService;
import com.challenge3.challenge3.compass.service.exceptions.RegraNegocioException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {


    private static PostRepository postRepository;

    private static PostClient postClient;

    private static CommentRepository commentRepository;



    public PostServiceImpl (PostRepository postRepository, PostClient postClient, CommentRepository commentRepository){
        this.postRepository = postRepository;
        this.postClient = postClient;
        this.commentRepository = commentRepository;
    }


    @Override
    public Post createPost(Long id) {
        validatePostId(id);
        Post postToCreate = postClient.getPostbyId(id);
        postToCreate.setCreateDate(LocalDate.now());
        postToCreate.setReprocessed(false);
        History history = new History();
        history.setProcessDate(LocalDate.now());
        history.setStatus(PostStatusEnum.CREATED);
        postToCreate.getHistory().add(history);
        return postRepository.save(postToCreate);
    }


    @Override
    public Post findPost(Long id) {
        validatePostId(id);
        Post postFinded = postRepository.findById(id).orElseThrow(
                () -> new RegraNegocioException("Não foi possível encontrar o post!")
        );
        History history = new History();
        history.setProcessDate(LocalDate.now());
        history.setStatus(PostStatusEnum.POST_FIND);
        postFinded.getHistory().add(history);
        return postRepository.save(postFinded);
    }

    @Override
    public Post validatePostFound(Long id){
        validatePostId(id);
        Post postToValidate = postRepository.findById(id).orElseThrow(
                () -> new RegraNegocioException("Não foi possível encontrar o post!")
        );
        if (getPostsById(id).get().getHistory().isEmpty()){
            History history = new History();
            history.setStatus(PostStatusEnum.FAILED);
            history.setProcessDate(LocalDate.now());
            postToValidate.getHistory().add(history);
            return postRepository.save(postToValidate);
        }
        else{
        History history = new History();
        history.setId(history.getId());
        history.setProcessDate(LocalDate.now());
        history.setStatus(PostStatusEnum.POST_OK);
        postToValidate.getHistory().add(history);
        }
        return postRepository.save(postToValidate);
    }


    @Override
    public Post findValidComments(Long id){
       Post postFounded = postRepository.findById(id).orElseThrow(
               () -> new RegraNegocioException("Não foi possível encontrar o post!")
       );

        List<Comment> comments = saveComments(id);
        if(comments.isEmpty()){
            History history = new History();
            history.setProcessDate(LocalDate.now());
            history.setStatus(PostStatusEnum.FAILED);
            postFounded.getHistory().add(history);
            return postRepository.save(postFounded);
        }
        else{
        History history = new History();
        history.setProcessDate(LocalDate.now());
        history.setStatus(PostStatusEnum.COMMENTS_FIND);
        postFounded.getHistory().add(history);
        }
        return postRepository.save(postFounded);
    }

    @Override
    public Post setCommentsIntoPostAndValidate(Long id){
        Post post = getPostsById(id).orElseThrow(
                () -> new RegraNegocioException("Não foi possível encontrar o post!")
        );
        List<Comment> listComments = saveComments(id);
        post.setComments(listComments);
        History history = new History();
        history.setProcessDate(LocalDate.now());
        history.setStatus(PostStatusEnum.COMMENTS_OK);
        post.getHistory().add(history);
        return postRepository.save(post);
    }

    @Override
    public List<Comment> saveComments(Long postId) {
        validatePostId(postId);
        List<Comment> comments = postClient.getCommentbyId(postId);
        List<Comment> savedComments = new ArrayList<>();
        for (Comment comment : comments) {
            Comment savedComment = commentRepository.save(comment);
            savedComments.add(savedComment);
        }
        return savedComments;
    }

    @Override
    public Post enablePost(Long id){
        Post post = getPostsById(id).orElseThrow(
                () -> new RegraNegocioException("Não foi possível encontrar o post!")
        );
        History history = new History();
        history.setProcessDate(LocalDate.now());
        history.setStatus(PostStatusEnum.ENABLED);
        post.getHistory().add(history);
        return postRepository.save(post);
    }

    @Override
    public Post disablePost(Long id){
        Post post = getPostsById(id).orElseThrow(
                () -> new RegraNegocioException("Não foi possível encontrar o post!")
        );
            History history = new History();
            history.setProcessDate(LocalDate.now());
            history.setStatus(PostStatusEnum.DISABLED);
            post.getHistory().add(history);
            return postRepository.save(post);
    }


    @Override
    public Post prepareToReprocessPost(Long id){
        Post post = getPostsById(id).orElseThrow(
                () -> new RegraNegocioException("Não foi possível encontrar o post!")
        );
        post.setReprocessed(true);
        History history = new History();
        history.setProcessDate(LocalDate.now());
        history.setStatus(PostStatusEnum.UPDATING);
        post.getHistory().add(history);
        return postRepository.save(post);
    }

    @Override
    public Post reprocessPost(Long id){
        Post post = getPostsById(id).orElseThrow(
                () -> new RegraNegocioException("Não foi possível encontrar o post!")
        );
        History history = new History();
        history.setProcessDate(LocalDate.now());
        history.setStatus(PostStatusEnum.POST_FIND);
        post.getHistory().add(history);
        return postRepository.save(post);
    }

    @Override
    public void validatePostId(Long id) {
        if (id == null || id < 1 || id > 100) {
            throw new RegraNegocioException("Informe um ID entre 1 e 100!");
        }
    }


    @Override
    public Optional<Post> getPostsById(Long id) {
        validatePostId(id);
        Optional<Post> postFinded = postRepository.findById(id);
        return postFinded;
    }



    @Override
    public List<Post> getAllPosts(int pageNo, int pageSize){
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Post> posts = postRepository.findAll(pageable);
        List<Post> postList = posts.getContent();
        return postList;
    }


}
