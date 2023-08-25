package com.challenge3.challenge3.compass.service.Impl;

import com.challenge3.challenge3.compass.client.PostClient;
import com.challenge3.challenge3.compass.model.Comment;
import com.challenge3.challenge3.compass.model.History;
import com.challenge3.challenge3.compass.model.Post;
import com.challenge3.challenge3.compass.model.enums.PostStatusEnum;
import com.challenge3.challenge3.compass.repository.PostRepository;
import com.challenge3.challenge3.compass.service.CommentService;
import com.challenge3.challenge3.compass.service.exceptions.RegraNegocioException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private PostRepository postRepository;
    private PostClient postClient;

    private PostServiceImpl postService;

    public CommentServiceImpl (PostRepository postRepository, PostClient postClient, PostServiceImpl postService){
        this.postRepository = postRepository;
        this.postClient = postClient;
        this.postService = postService;
    }


    @Override
    public Comment findComments(Long id, Post post){
        postService.validatePostId(id);
        Comment comments = postClient.getCommentbyId(id);

        if (comments == null) {
            throw new RegraNegocioException("Não foi possível encontrar comentários para este post.");
        }
        History history = new History();
        history.setStatus(PostStatusEnum.COMMENTS_FIND);
        post.getHistory().add(history);
        return comments;
    }
}
