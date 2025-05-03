package com.example.postservice.repository;

import com.example.postservice.model.entity.Comment;
import com.example.postservice.model.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;


public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select c from Comment c join fetch c.user where c.post = :post")
    Page<Comment> findAllByPost(Pageable pageable, Post post);

    @Transactional
    @Modifying
    @Query("update Comment c set c.deletedAt = now() where c.post = :post")
    void deleteAllByPost(Post post);
}
