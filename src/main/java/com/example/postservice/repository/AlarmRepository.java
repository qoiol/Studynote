package com.example.postservice.repository;

import com.example.postservice.model.entity.Alarm;
import com.example.postservice.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlarmRepository extends JpaRepository<Alarm, Long> {
    Page<Alarm> findByUser(Pageable pageable, User user);
}
