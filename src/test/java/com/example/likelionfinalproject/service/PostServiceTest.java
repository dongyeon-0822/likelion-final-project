package com.example.likelionfinalproject.service;

import com.example.likelionfinalproject.domain.dto.PostDto;
import com.example.likelionfinalproject.domain.entity.Post;
import com.example.likelionfinalproject.domain.entity.User;
import com.example.likelionfinalproject.domain.request.PostRequest;
import com.example.likelionfinalproject.exception.AppException;
import com.example.likelionfinalproject.exception.ErrorCode;
import com.example.likelionfinalproject.fixture.TestFixture;
import com.example.likelionfinalproject.repository.PostRepository;
import com.example.likelionfinalproject.repository.UserRepository;
import org.junit.jupiter.api.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.example.likelionfinalproject.fixture.PostFixture;

class PostServiceTest {
    private PostService postService;
    private PostRepository postRepository = mock(PostRepository.class);
    private UserRepository userRepository = mock(UserRepository.class);
    private TestFixture testInfo;

    @BeforeEach
    void setUp() {
        postService = new PostService(postRepository, userRepository);
        testInfo = TestFixture.builder()
                .postId(1l)
                .userId(1l)
                .userName("user")
                .password("password")
                .title("title")
                .body("body")
                .build();
    }

    @Test
    @DisplayName("포스트 등록 성공")
    void addPost_success() {
        // given
        Post mockPostEntity = mock(Post.class);
        User mockUserEntity = mock(User.class);

        //when
        when(userRepository.findByUserName(testInfo.getUserName()))
                .thenReturn(Optional.of(mockUserEntity));
        when(postRepository.save(any()))
                .thenReturn(mockPostEntity);

        //then
        Assertions.assertDoesNotThrow(() ->
                postService.addPost(testInfo.getUserName(), new PostRequest("title", "body")));
    }

    @Test
    @DisplayName("포스트 등록 실패 - 유저 존재 X")
    void addPost_fail1() {
        when(userRepository.findByUserName(testInfo.getUserName())).thenReturn(Optional.empty());
        when(postRepository.save(any())).thenReturn(mock(Post.class));

        AppException exception = Assertions.assertThrows(AppException.class, () -> postService.addPost(testInfo.getUserName(), new PostRequest("title", "body")));

        assertEquals(ErrorCode.USERNAME_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("포스트 조회 성공")
    void getPost_success() {
        // testfixture에서 가져오고
        // user entity 생성하고, username set하기
        // Post entity 에는 username, password set 하고 생성

        // post repo에서 fixture postid 로 찾고 post entity return

        // 그러면 post service 에서 get 한 결과랑 userName 이랑 같은지 비교
    }
}