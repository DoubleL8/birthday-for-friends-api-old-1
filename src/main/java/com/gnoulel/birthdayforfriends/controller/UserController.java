package com.gnoulel.birthdayforfriends.controller;

import com.gnoulel.birthdayforfriends.dto.UserDTO;
import com.gnoulel.birthdayforfriends.service.UserServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserServiceImpl userServiceImpl;

    public UserController(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @PostMapping("${api.auth.signup.url}")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO signup(@RequestBody UserDTO user) {
        return userServiceImpl.save(user);
    }

}
