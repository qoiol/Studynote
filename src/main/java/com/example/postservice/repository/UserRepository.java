package com.example.postservice.repository;

import com.example.postservice.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Modifying
    @Query("delete from User u where u.username = :id")
    void deleteUserFromDB(String id);

    Optional<User> findByUsername(String username);
}
