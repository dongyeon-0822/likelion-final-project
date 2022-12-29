package com.example.likelionfinalproject.controller;

import com.example.likelionfinalproject.service.HelloService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/hello")
public class HelloController {

    private final HelloService helloService;

    @GetMapping
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok().body("강동연");
    }

    @GetMapping("/{num}")
    public ResponseEntity<Integer> sumOfDigit(@PathVariable Integer num) {
        Integer result = helloService.sumOfDigit(num);
        return ResponseEntity.ok().body(result);
    }
}
