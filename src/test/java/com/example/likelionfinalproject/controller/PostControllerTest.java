package com.example.likelionfinalproject.controller;

import com.example.likelionfinalproject.domain.dto.PostDto;
import com.example.likelionfinalproject.domain.request.PostRequest;
import com.example.likelionfinalproject.exception.AppException;
import com.example.likelionfinalproject.exception.ErrorCode;
import com.example.likelionfinalproject.service.PostService;
import com.example.likelionfinalproject.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostController.class)
class PostControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PostService postService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("포스트 등록 성공")
    @WithMockUser
    void addPost() throws Exception {
        PostRequest postRequest = PostRequest.builder()
                .title("title")
                .body("body")
                .build();

        PostDto postDto = PostDto.builder()
                .id(0l)
                .build();

        when(postService.addPost(any(),any()))
                .thenReturn(postDto);

        mockMvc.perform(post("/api/v1/posts")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(postRequest)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result.message").exists())
            .andExpect(jsonPath("$.result.postId").exists());
    }

    @Test
    @DisplayName("포스트 등록 실패 - 로그인 하지 않은 경우")
    @WithAnonymousUser
    void addPost_fail1() throws Exception {
        PostRequest postRequest = PostRequest.builder()
                .title("title")
                .body("body")
                .build();

        when(postService.addPost(any(),any()))
                .thenThrow(new AppException(ErrorCode.INVALID_PERMISSION,ErrorCode.INVALID_PASSWORD.getMessage()));

        mockMvc.perform(post("/api/v1/posts")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(postRequest)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("포스트 상세 조회 성공")
    @WithMockUser
    void getPost() throws Exception {
        PostDto postDto = PostDto.builder()
                .id(1l)
                .title("title")
                .body("body")
                .userName("user")
                .createdAt(LocalDateTime.now())
                .lastModifiedAt(LocalDateTime.now())
                .build();

        when(postService.getPost(any()))
                .thenReturn(postDto);

        mockMvc.perform(get("/api/v1/posts/1")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.id").exists())
                .andExpect(jsonPath("$.result.title").exists())
                .andExpect(jsonPath("$.result.body").exists())
                .andExpect(jsonPath("$.result.userName").exists())
                .andExpect(jsonPath("$.result.createdAt").exists())
                .andExpect(jsonPath("$.result.lastModifiedAt").exists());

    }

    @Test
    @DisplayName("포스트 수정 성공")
    @WithMockUser
    void editPost() throws Exception {
        PostRequest postRequest = PostRequest.builder()
                .title("title")
                .body("body")
                .build();

        PostDto postDto = PostDto.builder()
                .id(1l)
                .title("title")
                .body("body")
                .userName("user")
                .createdAt(LocalDateTime.now())
                .lastModifiedAt(LocalDateTime.now())
                .build();


        when(postService.editPost(any(),any(),any()))
                .thenReturn(postDto);

        mockMvc.perform(put("/api/v1/posts/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(postRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.message").exists())
                .andExpect(jsonPath("$.result.postId").exists());

    }

    @Test
    void deletePost() {
    }
}