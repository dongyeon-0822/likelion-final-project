package com.example.likelionfinalproject.controller;

import com.example.likelionfinalproject.domain.response.AlarmResponse;
import com.example.likelionfinalproject.domain.response.Response;
import com.example.likelionfinalproject.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AlarmController {
    private final UserService userService;

    @GetMapping("/alarms")
    @ApiOperation(value = "알림 조회", notes = "로그인한 사용자는 특정 사용자가 게시한 게시물에 달린 댓글 및 좋아요에 대한 알림들을 한 페이지에 20개씩 조회 가능")
    public ResponseEntity<Response> getAlarms(Authentication authentication, Pageable pageable) {
        Page<AlarmResponse> alarms = userService.getAlarms(authentication.getName(), pageable);
        return ResponseEntity.ok().body(Response.success(alarms));
    }
}
