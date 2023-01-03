package com.example.likelionfinalproject.repository;

import com.example.likelionfinalproject.domain.entity.Likes;
import com.example.likelionfinalproject.domain.entity.Post;
import com.example.likelionfinalproject.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Likes, Long> {
    Optional<Likes> findByUserAndPost(User user, Post post);
}
