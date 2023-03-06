package com.gnoulel.birthdayforfriends.dto;


import com.gnoulel.birthdayforfriends.entity.User;
import com.gnoulel.birthdayforfriends.enums.RoleEnum;
import lombok.Data;

import java.util.Optional;

@Data
public class UserDTO {

    private Long id;
    private String email;
    private String password;
    private String name;
    private String role;

    /**
     * Convert User DTO to User Entity
     *
     * @param dto User DTO
     * @return User Entity
     */
    public static User to(UserDTO dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setEmail(dto.getEmail());
        user.setName(dto.getName());

        RoleEnum role = Optional.ofNullable(dto.getRole())
                .map(RoleEnum::valueOf)
                .orElse(null);
        user.setRole(role);

        return user;
    }

    /**
     * Convert User Entity to user DTO
     *
     * @param user User Entity
     * @return User DTO
     */
    public static UserDTO from(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setName(user.getName());
        dto.setRole(user.getRole().name());

        return dto;
    }

}
