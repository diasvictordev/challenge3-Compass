package com.challenge3.challenge3.compass.service;

import com.challenge3.challenge3.compass.model.History;
import com.challenge3.challenge3.compass.model.Post;

import java.util.List;
import java.util.Optional;

public interface PostService{

    Post createPost(Post post);

    Optional<Post> findPost(Post post, History history);

    void validatePost(Post post);
}
