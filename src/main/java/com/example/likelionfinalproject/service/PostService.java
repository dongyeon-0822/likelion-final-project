package com.example.likelionfinalproject.service;

import com.example.likelionfinalproject.domain.dto.PostDto;
import com.example.likelionfinalproject.domain.entity.Post;
import com.example.likelionfinalproject.domain.entity.User;
import com.example.likelionfinalproject.domain.request.PostRequest;
import com.example.likelionfinalproject.domain.response.PostResponse;
import com.example.likelionfinalproject.exception.AppException;
import com.example.likelionfinalproject.exception.ErrorCode;
import com.example.likelionfinalproject.repository.PostRepository;
import com.example.likelionfinalproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostDto addPost(String userName, PostRequest postRequest) {
        User user = userRepository.findByUserName(userName)
                .orElseThrow(()-> new AppException(ErrorCode.USERNAME_NOT_FOUND, ErrorCode.DUPLICATED_USER_NAME.getMessage()));
        Post savedPost = postRepository.save(Post.of(postRequest.getTitle(), postRequest.getBody(), user));
        return PostDto.toPostDto(savedPost);
    }
}
