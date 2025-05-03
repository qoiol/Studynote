package com.example.postservice.repository;

import com.example.postservice.model.entity.Post;
import com.example.postservice.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("select p from Post p join fetch p.user where p.user.id = :userId")
    Page<Post> findAllByUserId(Integer userId, Pageable pageable);

    @Query("select p from Post p join fetch p.user")
    Page<Post> findAllPosts(Pageable pageable);
}
