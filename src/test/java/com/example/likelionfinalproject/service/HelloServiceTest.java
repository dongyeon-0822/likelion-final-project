package com.example.likelionfinalproject.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HelloServiceTest {
    HelloService helloService = new HelloService();

    @Test
    void sumOfDigit() {
        assertEquals(21, helloService.sumOfDigit(678));
        assertEquals(0, helloService.sumOfDigit(0));
    }
}