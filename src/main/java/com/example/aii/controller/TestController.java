package com.example.aii.controller;

import com.example.aii.entity.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @PostMapping("/test")
    public User getUser(@RequestBody User user) {
        return user;
    }
}
