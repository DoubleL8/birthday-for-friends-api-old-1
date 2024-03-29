package com.gnoulel.birthdayforfriends.controller;

import com.gnoulel.birthdayforfriends.dto.UserDTO;
import com.gnoulel.birthdayforfriends.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("${api.auth.signup.url}")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO signup(@RequestBody UserDTO user) {
        return userService.save(user);
    }

}
