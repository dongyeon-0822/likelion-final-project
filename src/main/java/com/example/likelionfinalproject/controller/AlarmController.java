package com.example.likelionfinalproject.controller;

import com.example.likelionfinalproject.domain.response.AlarmResponse;
import com.example.likelionfinalproject.domain.response.Response;
import com.example.likelionfinalproject.service.UserService;
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
    public ResponseEntity<Response> getAlarms(Authentication authentication, Pageable pageable) {
        Page<AlarmResponse> alarms = userService.getAlarms(authentication.getName(), pageable);
        return ResponseEntity.ok().body(Response.success(alarms));
    }
}
