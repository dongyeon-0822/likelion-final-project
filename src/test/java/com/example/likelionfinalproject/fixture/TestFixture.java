package com.example.likelionfinalproject.fixture;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TestFixture {
    private Long postId;
    private Long userId;
    private String userName;
    private String password;
    private String title;
    private String body;
}
