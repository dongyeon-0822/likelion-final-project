package com.example.likelionfinalproject.service;

import com.example.likelionfinalproject.domain.dto.PostDto;
import com.example.likelionfinalproject.domain.entity.Post;
import com.example.likelionfinalproject.domain.entity.User;
import com.example.likelionfinalproject.domain.request.PostRequest;
import com.example.likelionfinalproject.exception.AppException;
import com.example.likelionfinalproject.exception.ErrorCode;
import com.example.likelionfinalproject.repository.PostRepository;
import com.example.likelionfinalproject.repository.UserRepository;
import lombok.*;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.example.likelionfinalproject.testInfo.TestInfo;

class PostServiceTest {
    PostService postService;
    PostRepository postRepository = mock(PostRepository.class);
    UserRepository userRepository = mock(UserRepository.class);

    @BeforeEach
    void setUp() {
        postService = new PostService(postRepository, userRepository);
    }

    @Test
    @DisplayName("포스트 등록 성공")
    void addPost_success() {
        TestInfo.TestInfoInner testInfo = TestInfo.get();
        Post mockPostEntity = mock(Post.class);
        User mockUserEntity = mock(User.class);

        when(userRepository.findByUserName(testInfo.getUserName()))
                .thenReturn(Optional.of(mockUserEntity));
        when(postRepository.save(any()))
                .thenReturn(mockPostEntity);

        Assertions.assertDoesNotThrow(() ->
                postService.addPost(testInfo.getTitle(), new PostRequest("title", "body")));
    }

    @Test
    @DisplayName("포스트 등록 실패 - 유저 존재 X")
    void addPost_fail1() {
        TestInfo.TestInfoInner testInfo = TestInfo.get();
        when(userRepository.findByUserName(testInfo.getUserName())).thenReturn(Optional.empty());
        when(postRepository.save(any())).thenReturn(mock(Post.class));
        AppException exception = Assertions.assertThrows(AppException.class, () -> postService.addPost(testInfo.getUserName(), new PostRequest("title", "body")));

        assertEquals(ErrorCode.USERNAME_NOT_FOUND, ErrorCode.USERNAME_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("포스트 조회 성공")
    void getPost_success() {
        TestInfo.TestInfoInner testInfo = TestInfo.get();
        User userEntity = new User();
        userEntity.setUserName(testInfo.getUserName());

        Post postEntity = Post.builder()
                .id(testInfo.getPostId())
                .user(userEntity)
                .title(testInfo.getTitle())
                .body(testInfo.getBody())
                .build();

        when(postRepository.findById(testInfo.getPostId())).thenReturn(Optional.of(postEntity));

        PostDto postDto = postService.getPost(testInfo.getPostId());
        assertEquals(testInfo.getUserName(), postDto.getUserName());
    }
}