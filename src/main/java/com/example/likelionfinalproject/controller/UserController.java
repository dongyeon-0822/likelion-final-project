package com.example.likelionfinalproject.controller;

import com.example.likelionfinalproject.domain.dto.PostDto;
import com.example.likelionfinalproject.domain.entity.User;
import com.example.likelionfinalproject.domain.request.UserJoinRequest;
import com.example.likelionfinalproject.domain.request.UserLoginRequest;
import com.example.likelionfinalproject.domain.response.AlarmResponse;
import com.example.likelionfinalproject.domain.response.Response;
import com.example.likelionfinalproject.domain.response.UserJoinResponse;
import com.example.likelionfinalproject.domain.response.UserLoginResponse;
import com.example.likelionfinalproject.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    @ApiOperation(value = "회원가입", notes = "userName과 password로 회원가입, 단 userName 중복 불가능")
    public ResponseEntity<Response> join(@RequestBody UserJoinRequest dto) {
        UserJoinResponse userJoinResponse = userService.join(dto.getUserName(), dto.getPassword());
        return ResponseEntity.ok().body(Response.success(userJoinResponse));
    }

    @PostMapping("/login")
    @ApiOperation(value = "로그인", notes = "userName과 password로 로그인 후 token 발급")
    public ResponseEntity<Response> login(@RequestBody UserLoginRequest dto) {
        UserLoginResponse userLoginResponse = userService.login(dto.getUserName(), dto.getPassword());
        return ResponseEntity.ok().body(Response.success(userLoginResponse));
    }
}
