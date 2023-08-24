package com.challenge3.challenge3.compass.client;


import com.challenge3.challenge3.compass.model.Comment;
import com.challenge3.challenge3.compass.model.Post;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(value = "posts", url = "https://jsonplaceholder.typicode.com/posts")
public interface CommentClient {

    @GetMapping(value = "/{id}/comments")
    Optional<Comment> getCommentbyId(@PathVariable("id") Long id);
}
