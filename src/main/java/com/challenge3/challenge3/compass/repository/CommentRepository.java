package com.challenge3.challenge3.compass.repository;

import com.challenge3.challenge3.compass.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
