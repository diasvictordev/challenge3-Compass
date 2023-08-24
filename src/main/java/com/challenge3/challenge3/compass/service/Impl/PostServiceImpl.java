package com.challenge3.challenge3.compass.service.Impl;

import com.challenge3.challenge3.compass.client.PostClient;
import com.challenge3.challenge3.compass.model.History;
import com.challenge3.challenge3.compass.model.Post;
import com.challenge3.challenge3.compass.model.enums.PostStatusEnum;
import com.challenge3.challenge3.compass.repository.PostRepository;
import com.challenge3.challenge3.compass.service.PostService;
import com.challenge3.challenge3.compass.service.exceptions.RegraNegocioException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {

    private PostRepository postRepository;
    private PostClient postClient;

    public PostServiceImpl (PostRepository postRepository, PostClient postClient){
        this.postRepository = postRepository;
        this.postClient = postClient;
    }



    @Override
    public Post createPost(Post post){
        validatePost(post);
        History history = new History();
        history.setStatus(PostStatusEnum.CREATED);
        history.setCreateDate(LocalDate.now());
        post.getHistory().add(history);
        return postRepository.save(post);
    }

    @Override
    public Optional<Post> findPost(Post post, History history){
        Optional<Post> postFound = postClient.getPostbyId(post.getId());
        history.setStatus(PostStatusEnum.POST_FIND);
        if (postFound.isPresent()) {
            history.setStatus(PostStatusEnum.POST_OK);
            return postFound;
        } else {
            history.setStatus(PostStatusEnum.FAILED);
            throw new RegraNegocioException("Post não encontrado. Tente novamente!");
        }
    }

    @Override
    public void validatePost(Post post){
        if (post.getId() < 1 || post.getId() > 100 || post.getId() == null){
            throw new RegraNegocioException("Informe um id entre 1 e 100!");
        }
    }


}
