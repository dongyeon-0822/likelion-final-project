package com.example.likelionfinalproject.testInfo;

import lombok.Getter;
import lombok.Setter;

public class TestInfo {
    public static TestInfoInner get() {
        TestInfoInner info = new TestInfoInner();
        info.setPostId(1l);
        info.setUserId(1l);
        info.setUserName("user");
        info.setPassword("password");
        info.setTitle("title");
        info.setBody("body");
        return info;
    }

    @Getter
    @Setter
    public static class TestInfoInner {
        private Long postId;
        private Long userId;
        private String userName;
        private String password;
        private String title;
        private String body;
    }
}