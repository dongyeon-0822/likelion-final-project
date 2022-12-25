package com.example.likelionfinalproject.domain.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PostAddRequest {
    private String title;
    private String body;
}
