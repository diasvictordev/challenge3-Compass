package com.challenge3.challenge3.compass.service;

import com.challenge3.challenge3.compass.model.Comment;
import com.challenge3.challenge3.compass.model.History;
import com.challenge3.challenge3.compass.model.Post;


import java.util.List;
import java.util.Optional;

public interface PostService{

    Post createPost(Long id);

    Post findPost(Long id);

    Post validatePostFound(Long id);

    Post findValidComments(Long id);

    Post enablePost(Long id);

    Post disablePost(Long id);

    Post prepareToReprocessPost(Long id);

    Post reprocessPost(Long id);

    void validatePostId(Long id);


    Optional<Post> getPostsById(Long id);

    List<Post> getAllPosts(int pageNo, int pageSize);

    Post setCommentsIntoPostAndValidate(Long id);

    List<Comment> saveComments(Long postId);
}
