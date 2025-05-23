package com.example.postservice.repository;

import com.example.postservice.model.entity.Post;
import com.example.postservice.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAllByUser(User user, Pageable pageable);
}
