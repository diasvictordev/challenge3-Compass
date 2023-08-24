package com.challenge3.challenge3.compass.service.Impl;

import com.challenge3.challenge3.compass.client.CommentClient;
import com.challenge3.challenge3.compass.model.Comment;
import com.challenge3.challenge3.compass.model.History;
import com.challenge3.challenge3.compass.model.enums.PostStatusEnum;
import com.challenge3.challenge3.compass.repository.CommentRepository;
import com.challenge3.challenge3.compass.service.CommentService;
import com.challenge3.challenge3.compass.service.exceptions.RegraNegocioException;

import java.util.Optional;

public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;
    private CommentClient commentClient;

    public CommentServiceImpl (CommentRepository commentRepository, CommentClient commentClient){
        this.commentRepository = commentRepository;
        this.commentClient = commentClient;
    }


    @Override
    public Optional<Comment> findComment(Comment comment, History history){
        Optional<Comment> commentFound = commentClient.getCommentbyId(comment.getId());
        history.setStatus(PostStatusEnum.COMMENTS_FIND);
        if (commentFound.isPresent()) {
            history.setStatus(PostStatusEnum.COMMENTS_OK);
            return commentFound;
        } else {
            history.setStatus(PostStatusEnum.FAILED);
            throw new RegraNegocioException("Nenhum comentário encontrado!");
        }
    }
}
