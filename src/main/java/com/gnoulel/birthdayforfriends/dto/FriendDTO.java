package com.gnoulel.birthdayforfriends.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gnoulel.birthdayforfriends.entity.Friend;
import com.gnoulel.birthdayforfriends.enums.GenderEnum;
import com.gnoulel.birthdayforfriends.utils.AppUtils;
import lombok.Data;

import java.time.LocalDate;

@Data
public class FriendDTO {
    private Long id;
    private String name;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthdate;
    private String gender;
    private String email;
    private String phone;
    private String note;

    /**
     * Convert Friend Entity to Friend DTO
     *
     * @param friend Friend Entity
     * @return Friend DTO
     */
    public static FriendDTO from(Friend friend) {
        FriendDTO dto = new FriendDTO();
        dto.setId(friend.getId());
        dto.setName(friend.getName());
        dto.setBirthdate(friend.getBirthdate());
        dto.setGender(friend.getGender().name());
        dto.setEmail(friend.getEmail());
        dto.setPhone(friend.getPhone());
        dto.setNote(friend.getNote());

        return dto;
    }

    /**
     * Convert Friend DTO to Friend Entity
     * @param dto Friend DTO
     * @return Friend Entity
     */
    public static Friend to(FriendDTO dto) {
        Friend friend = new Friend();
        friend.setId(dto.getId());
        friend.setName(dto.getName());
        friend.setBirthdate(dto.getBirthdate());
        friend.setEmail(dto.getEmail());
        friend.setPhone(dto.getPhone());
        friend.setNote(dto.getNote());

        GenderEnum gender = AppUtils.getGender(dto.getGender());
        friend.setGender(gender);

        return friend;
    }
}
