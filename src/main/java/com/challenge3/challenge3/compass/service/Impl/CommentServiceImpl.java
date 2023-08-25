package com.challenge3.challenge3.compass.service.Impl;

import com.challenge3.challenge3.compass.client.PostClient;
import com.challenge3.challenge3.compass.model.Comment;
import com.challenge3.challenge3.compass.repository.CommentRepository;
import com.challenge3.challenge3.compass.service.CommentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;
    private PostClient postClient;

    public CommentServiceImpl (CommentRepository commentRepository, PostClient postClient){
        this.commentRepository = commentRepository;
        this.postClient = postClient;
    }

    @Override
    public Comment getCommentsById(Long id) {
        return postClient.getCommentbyId(id);
    }
}
