package com.challenge3.challenge3.compass.service.Impl;

import com.challenge3.challenge3.compass.client.PostClient;
import com.challenge3.challenge3.compass.model.Comment;
import com.challenge3.challenge3.compass.model.History;
import com.challenge3.challenge3.compass.model.Post;
import com.challenge3.challenge3.compass.model.enums.PostStatusEnum;
import com.challenge3.challenge3.compass.repository.PostRepository;
import com.challenge3.challenge3.compass.service.PostService;
import com.challenge3.challenge3.compass.service.exceptions.RegraNegocioException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {


    private PostRepository postRepository;

    private PostClient postClient;



    public PostServiceImpl (PostRepository postRepository, PostClient postClient){
        this.postRepository = postRepository;
        this.postClient = postClient;
    }

    @Override
    public Post createPost(Long id) {
        validatePostId(id);
        Post postToCreate = postClient.getPostbyId(id);
        postToCreate.setProcessDate(LocalDate.now());
        History history = new History();
        history.setCreateDate(LocalDate.now());
        history.setStatus(PostStatusEnum.CREATED);
        postToCreate.getHistory().add(history);
        return postRepository.save(postToCreate);
    }

    @Override
    public Post findPost(Long id, Post postCreated) {
        if (postCreated == null) {
            throw new RegraNegocioException("Post não encontrado. Tente novamente!");
        }
        // Corrigir
        History history = postCreated.getHistory().get(2);
        history.setStatus(PostStatusEnum.POST_FIND);
        postCreated.getHistory().add(history);
        return postRepository.save(postCreated);
    }


    @Override
    public void validatePostId(Long id) {
        if (id == null || id < 1 || id > 100) {
            throw new RegraNegocioException("Informe um ID entre 1 e 100!");
        }
    }

    @Override
    public Post getPostsById(Long id) {
        validatePostId(id);
        return postClient.getPostbyId(id);
    }


    @Override
    public List<Post> getAllPosts(){
        return postRepository.findAll();
    }


}
