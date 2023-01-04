package com.example.likelionfinalproject.repository;

import com.example.likelionfinalproject.domain.entity.Alarm;
import com.example.likelionfinalproject.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlarmRepository extends JpaRepository<Alarm, Long> {
    Page<Alarm> findAllByUser(User user, Pageable pageable);
}
