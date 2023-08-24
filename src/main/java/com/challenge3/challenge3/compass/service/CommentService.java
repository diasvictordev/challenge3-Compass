package com.challenge3.challenge3.compass.service;

import com.challenge3.challenge3.compass.model.Comment;
import com.challenge3.challenge3.compass.model.History;

import java.util.Optional;

public interface CommentService {
    Optional<Comment> findComment(Comment comment, History history);
}
