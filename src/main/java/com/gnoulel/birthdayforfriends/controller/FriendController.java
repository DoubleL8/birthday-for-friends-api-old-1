package com.gnoulel.birthdayforfriends.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FriendController {

    @GetMapping("${api.friends-management.url}")
    public String getFriendInfo() {
        return "hello";
    }

}
