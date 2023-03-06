package com.gnoulel.birthdayforfriends.service;

import com.gnoulel.birthdayforfriends.dto.UserDTO;
import com.gnoulel.birthdayforfriends.entity.User;

public interface UserService {
    UserDTO save(UserDTO userDTO);
}
