package com.challenge3.challenge3.compass.service;

import com.challenge3.challenge3.compass.model.History;
import com.challenge3.challenge3.compass.model.Post;

import java.util.List;

public interface PostService{

    Post createPost(Long id);

    Post findPost(Long id, Post postCreated);

    void validatePostId(Long id);

    Post getPostsById(Long id);

    List<Post> getAllPosts();
}
