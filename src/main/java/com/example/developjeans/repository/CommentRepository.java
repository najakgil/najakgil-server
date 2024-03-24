package com.example.developjeans.repository;

import com.example.developjeans.entity.Comment;
import com.example.developjeans.entity.Photo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT p FROM Comment p WHERE p.id < :lastPageId ORDER BY p.createdAt DESC")
    Page<Comment> findAllByOrderByCreatedAtDesc(Long lastPageId, PageRequest pageRequest);

    @Query("SELECT p FROM Comment p WHERE p.id < :lastPageId ORDER BY p.likes_cnt DESC")
    Page<Comment> findAllByOrderByLikesDesc(Long lastPageId, PageRequest pageRequest);
}
