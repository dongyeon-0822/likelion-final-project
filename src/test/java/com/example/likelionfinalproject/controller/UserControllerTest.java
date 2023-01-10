package com.example.likelionfinalproject.controller;

import com.example.likelionfinalproject.domain.entity.AlarmType;
import com.example.likelionfinalproject.domain.request.UserJoinRequest;
import com.example.likelionfinalproject.domain.request.UserLoginRequest;
import com.example.likelionfinalproject.domain.response.AlarmResponse;
import com.example.likelionfinalproject.domain.response.UserJoinResponse;
import com.example.likelionfinalproject.domain.response.UserLoginResponse;
import com.example.likelionfinalproject.exception.AppException;
import com.example.likelionfinalproject.exception.ErrorCode;
import com.example.likelionfinalproject.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("회원가입 성공")
    @WithMockUser
    void join() throws Exception {
        Long id = 0l;
        String userName = "dong";
        String password = "0000";

        when(userService.join(any(),any()))
                .thenReturn(new UserJoinResponse(id,userName));

        mockMvc.perform(post("/api/v1/users/join")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserJoinRequest(userName,password))))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("회원가입 실패 - userName 중복")
    @WithMockUser
    void join_fail() throws Exception {
        String userName = "dong";
        String password = "0000";

        when(userService.join(any(), any()))
                .thenThrow(new AppException(ErrorCode.DUPLICATED_USER_NAME, ErrorCode.DUPLICATED_USER_NAME.getMessage()));

        mockMvc.perform(post("/api/v1/users/join")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserJoinRequest(userName,password))))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.result").exists())
                .andExpect(jsonPath("$.result.message").exists());
    }

    @Test
    @DisplayName("로그인 성공")
    @WithMockUser
    void login_success() throws Exception {
        String userName = "dong";
        String password = "0000";
        String jwt = "token";

        when(userService.login(any(), any()))
                .thenReturn(new UserLoginResponse(jwt));

        mockMvc.perform(post("/api/v1/users/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserLoginRequest(userName,password))))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.jwt").exists());
    }

    @Test
    @DisplayName("로그인 실패 - no userName")
    @WithMockUser
    void login_fail_userName() throws Exception {
        String userName = "dong";
        String password = "0000";

        when(userService.login(any(), any()))
                .thenThrow(new AppException(ErrorCode.USERNAME_NOT_FOUND, ErrorCode.DUPLICATED_USER_NAME.getMessage()));

        mockMvc.perform(post("/api/v1/users/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserLoginRequest(userName,password))))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.result").exists())
                .andExpect(jsonPath("$.result.message").exists());
    }
    @Test
    @DisplayName("로그인 실패 - incorrect password")
    @WithMockUser
    void login_fail_password() throws Exception {
        String userName = "dong";
        String password = "0000";

        when(userService.login(any(), any()))
                .thenThrow(new AppException(ErrorCode.INVALID_PASSWORD, ErrorCode.INVALID_PASSWORD.getMessage()));

        mockMvc.perform(post("/api/v1/users/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserLoginRequest(userName,password))))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.result").exists())
                .andExpect(jsonPath("$.result.message").exists());
    }

    @Test
    @DisplayName("알람목록 조회 성공")
    @WithMockUser
    void getAlarms_success() throws Exception {
        AlarmResponse alarmResponse = AlarmResponse.builder()
                .id(1l)
                .alarmType(AlarmType.NEW_COMMENT_ON_POST)
                .targetId(1l)
                .fromUserId(1l)
                .text("text")
                .createdAt(LocalDateTime.now())
                .build();
        Page<AlarmResponse> alarmResponses = new PageImpl<>(List.of(alarmResponse));

        when(userService.getAlarms(any(),any()))
                .thenReturn(alarmResponses);

        mockMvc.perform(get("/api/v1/users/alarms")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").exists())
                .andExpect(jsonPath("$.result.content[0].id").exists());
    }

    @Test
    @DisplayName("알람목록 조회 실패 - 로그인하지 않은 경우")
    @WithAnonymousUser
    void getAlarms_fail() throws Exception {
        Page<AlarmResponse> alarmResponses = Page.empty();

        when(userService.getAlarms(any(),any()))
                .thenReturn(alarmResponses);

        mockMvc.perform(get("/api/v1/users/alarms")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}