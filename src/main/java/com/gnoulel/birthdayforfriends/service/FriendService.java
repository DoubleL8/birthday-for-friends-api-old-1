package com.gnoulel.birthdayforfriends.service;

import com.gnoulel.birthdayforfriends.dto.FriendDTO;
import com.gnoulel.birthdayforfriends.dto.PageDTO;
import org.springframework.data.domain.Pageable;

public interface FriendService {
    FriendDTO addFriendToUser(String username, FriendDTO friendDTO);
    void updateFriendBelongToUser(FriendDTO friendDTO, String username);
    void deleteFriendBelongToUser(Long id, String username);
    PageDTO<FriendDTO> getFriendsBelongToUser(String username, Pageable pageable);
    FriendDTO getFriendBelongToUser(Long id, String username);
}
