package com.example.likelionfinalproject.service;

import com.example.likelionfinalproject.domain.entity.*;
import com.example.likelionfinalproject.exception.AppException;
import com.example.likelionfinalproject.exception.ErrorCode;
import com.example.likelionfinalproject.repository.AlarmRepository;
import com.example.likelionfinalproject.repository.LikeRepository;
import com.example.likelionfinalproject.repository.PostRepository;
import com.example.likelionfinalproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final AlarmRepository alarmRepository;

    public Long likePost(String userName, Long postId) {
        User user = userRepository.findByUserName(userName)
                .orElseThrow(()-> new AppException(ErrorCode.USERNAME_NOT_FOUND, ErrorCode.USERNAME_NOT_FOUND.getMessage()));

        Post post = postRepository.findById(postId)
                .orElseThrow(()-> new AppException(ErrorCode.POST_NOT_FOUND, ErrorCode.POST_NOT_FOUND.getMessage()));

        likeRepository.findByUserAndPost(user, post)
                    .ifPresent(likes -> {
                        throw new AppException(ErrorCode.DUPLICATED_LIKES, ErrorCode.DUPLICATED_LIKES.getMessage());
                    });
        alarmRepository.save(Alarm.of(AlarmType.NEW_LIKE_ON_POST, post.getUser(), user.getUserId(), postId, AlarmType.NEW_LIKE_ON_POST.getAlarmText()));
        Likes likes = likeRepository.save(Likes.of(user,post));
        return likes.getId();
    }

    public Long countLikes(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(()-> new AppException(ErrorCode.POST_NOT_FOUND, ErrorCode.POST_NOT_FOUND.getMessage()));
        Long likes = likeRepository.countByPost(post);
        return likes;
    }

    public Long unlikePost(String userName, Long postId) {
        User user = userRepository.findByUserName(userName)
                .orElseThrow(()-> new AppException(ErrorCode.USERNAME_NOT_FOUND, ErrorCode.USERNAME_NOT_FOUND.getMessage()));

        Post post = postRepository.findById(postId)
                .orElseThrow(()-> new AppException(ErrorCode.POST_NOT_FOUND, ErrorCode.POST_NOT_FOUND.getMessage()));

        Likes likes = likeRepository.findByUserAndPost(user, post)
                        .orElseThrow(()-> new AppException(ErrorCode.LIKES_NOT_FOUND, ErrorCode.LIKES_NOT_FOUND.getMessage()));
        likeRepository.delete(likes);
        return postId;
    }
}
