package com.challenge3.challenge3.compass.repository;

import com.challenge3.challenge3.compass.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
