package com.example.likelionfinalproject.controller;

import com.example.likelionfinalproject.domain.entity.User;
import com.example.likelionfinalproject.domain.request.UserJoinRequest;
import com.example.likelionfinalproject.domain.request.UserLoginRequest;
import com.example.likelionfinalproject.domain.response.Response;
import com.example.likelionfinalproject.domain.response.UserJoinResponse;
import com.example.likelionfinalproject.domain.response.UserLoginResponse;
import com.example.likelionfinalproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public ResponseEntity<Response> join(@RequestBody UserJoinRequest dto) {
        UserJoinResponse userJoinResponse = userService.join(dto.getUserName(), dto.getPassword());
        return ResponseEntity.ok().body(Response.success(userJoinResponse));
    }

    @PostMapping("/login")
    public ResponseEntity<Response> login(@RequestBody UserLoginRequest dto) {
        UserLoginResponse userLoginResponse = userService.login(dto.getUserName(), dto.getPassword());
        return ResponseEntity.ok().body(Response.success(userLoginResponse));
    }
}
