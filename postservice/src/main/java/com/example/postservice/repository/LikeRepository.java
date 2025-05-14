package com.example.postservice.repository;

import com.example.postservice.model.entity.Like;
import com.example.postservice.model.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    long countByPost(Post post);

    @Transactional
    @Modifying
    @Query("update Like l set l.deletedAt = now() where l.post = :post")
    void deleteAllByPost(Post post);

    Optional<Like> findByUserIdAndPost(Integer userId, Post post);
}
