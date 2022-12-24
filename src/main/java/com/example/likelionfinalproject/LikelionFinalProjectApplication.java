package com.example.likelionfinalproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class LikelionFinalProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(LikelionFinalProjectApplication.class, args);
    }

}
