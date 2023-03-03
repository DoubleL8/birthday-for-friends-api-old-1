package com.gnoulel.birthdayforfriends.controller;

import com.gnoulel.birthdayforfriends.dto.UserDTO;
import com.gnoulel.birthdayforfriends.service.UserServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class UserController {

    private UserServiceImpl userServiceImpl;

    public UserController(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @PostMapping("${api.auth.signup.url}")
    public ResponseEntity<String> signup(@RequestBody UserDTO user) {
        userServiceImpl.save(user);
        return ResponseEntity.status(201).body("User created successfully!");
    }

}
