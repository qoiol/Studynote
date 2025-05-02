package com.example.postservice.repository;

import com.example.postservice.domain.Like;
import com.example.postservice.domain.Post;
import com.example.postservice.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserAndPost(User user, Post post);

    Integer countByPost(Post post);
}
