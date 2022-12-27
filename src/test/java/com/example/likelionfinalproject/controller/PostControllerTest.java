package com.example.likelionfinalproject.controller;

import com.example.likelionfinalproject.domain.dto.PostDto;
import com.example.likelionfinalproject.domain.request.PostRequest;
import com.example.likelionfinalproject.service.PostService;
import com.example.likelionfinalproject.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
                .andExpect(jsonPath("$.result.message").exists());

    }

    @Test
    void getPostList() {
    }

    @Test
    void getPost() {
    }

    @Test
    void editPost() {
    }

    @Test
    void deletePost() {
    }
}