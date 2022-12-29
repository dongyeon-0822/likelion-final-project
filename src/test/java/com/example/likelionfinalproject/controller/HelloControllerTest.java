package com.example.likelionfinalproject.controller;

import com.example.likelionfinalproject.service.HelloService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HelloController.class)
class HelloControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    HelloService helloService;

    @Test
    @WithMockUser
    @DisplayName("자릿수 합 구하기")
    void sumOfDigit() throws Exception {
        when(helloService.sumOfDigit(any()))
                .thenReturn(6);

        mockMvc.perform(get("/api/v1/hello/123")
                    .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("6"));
    }
}