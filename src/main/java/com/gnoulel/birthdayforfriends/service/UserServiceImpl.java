package com.gnoulel.birthdayforfriends.service;

import com.gnoulel.birthdayforfriends.dto.UserDTO;
import com.gnoulel.birthdayforfriends.enums.RoleEnum;
import com.gnoulel.birthdayforfriends.entity.User;
import com.gnoulel.birthdayforfriends.exception.UserExistedException;
import com.gnoulel.birthdayforfriends.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDTO save(UserDTO userDTO) {
        boolean isUserExisted = userRepository.existsUserByEmail(userDTO.getEmail());

        if (isUserExisted) {
            throw new UserExistedException("Email is already used!");
        }

        User user = UserDTO.to(userDTO);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setRole(RoleEnum.USER_ROLE);

        User savedUser = userRepository.save(user);

        return UserDTO.from(savedUser);
    }
}
