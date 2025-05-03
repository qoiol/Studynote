package com.example.postservice.repository;

import com.example.postservice.model.entity.Like;
import com.example.postservice.model.entity.Post;
import com.example.postservice.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserAndPost(User user, Post post);

//    @Query("select count(l.id) from Like l where l.post=:post")
    long countByPost(Post post);
}
