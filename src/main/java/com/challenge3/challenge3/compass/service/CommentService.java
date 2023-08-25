package com.challenge3.challenge3.compass.service;

import com.challenge3.challenge3.compass.model.Comment;
import com.challenge3.challenge3.compass.model.History;
import com.challenge3.challenge3.compass.model.Post;

import java.util.List;

public interface CommentService {

    Comment findComments(Long id, Post post);
}
