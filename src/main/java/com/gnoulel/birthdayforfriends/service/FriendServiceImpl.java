package com.gnoulel.birthdayforfriends.service;

import com.gnoulel.birthdayforfriends.constants.AppConstant;
import com.gnoulel.birthdayforfriends.dto.FriendDTO;
import com.gnoulel.birthdayforfriends.dto.PageDTO;
import com.gnoulel.birthdayforfriends.entity.Friend;
import com.gnoulel.birthdayforfriends.entity.User;
import com.gnoulel.birthdayforfriends.enums.GenderEnum;
import com.gnoulel.birthdayforfriends.exception.EmailExistedException;
import com.gnoulel.birthdayforfriends.exception.FriendNotFoundException;
import com.gnoulel.birthdayforfriends.exception.PhoneExistedException;
import com.gnoulel.birthdayforfriends.repository.FriendRepository;
import com.gnoulel.birthdayforfriends.repository.UserRepository;
import com.gnoulel.birthdayforfriends.utils.AppUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class FriendServiceImpl implements FriendService {
    private final FriendRepository friendRepository;
    private final UserRepository userRepository;

    public FriendServiceImpl(FriendRepository friendRepository, UserRepository userRepository) {
        this.friendRepository = friendRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public FriendDTO addFriendToUser(String username, FriendDTO friendDTO) {
        // Check is friend's email of logged user is existed
        if (StringUtils.isNotBlank(friendDTO.getEmail())
                && friendRepository.isEmailExistedBelongToUser(username, friendDTO.getEmail())) {
            throw new EmailExistedException(AppConstant.EMAIL_FRIEND_EXISTED);
        }

        // Check is friend's phone of logged user is existed
        if (StringUtils.isNotBlank(friendDTO.getPhone())
                && friendRepository.isPhoneExistedBelongToUser(username, friendDTO.getPhone())) {
            throw new PhoneExistedException(AppConstant.PHONE_FRIEND_EXISTED);
        }

        User user = userRepository.findByEmail(username).get();
        Friend friend = FriendDTO.to(friendDTO);
        friend.setUser(user);

        Friend saveFriend = friendRepository.save(friend);
        return FriendDTO.from(saveFriend);
    }

    @Transactional
    @Override
    public void updateFriendBelongToUser(FriendDTO friendDTO, String username) {
        Optional<Friend> friendOptional =
                friendRepository.findFriendByIdAndUserEmail(friendDTO.getId(), username);

        friendOptional.ifPresentOrElse(friend -> {
            // Check is friend's email of logged user is existed
            if (StringUtils.isNotBlank(friend.getEmail())
                    && StringUtils.isNotBlank(friendDTO.getEmail())
                    && !StringUtils.equalsIgnoreCase(friend.getEmail(), friendDTO.getEmail())
                    && friendRepository.isEmailExistedBelongToUser(username, friendDTO.getEmail())) {
                throw new EmailExistedException(AppConstant.EMAIL_FRIEND_EXISTED);
            }

            // Check is friend's phone of logged user is existed
            if (StringUtils.isNotBlank(friend.getPhone())
                    && StringUtils.isNotBlank(friendDTO.getPhone())
                    && !StringUtils.equalsIgnoreCase(friend.getPhone(), friendDTO.getPhone())
                    && friendRepository.isPhoneExistedBelongToUser(username, friendDTO.getPhone())) {
                throw new PhoneExistedException(AppConstant.PHONE_FRIEND_EXISTED);
            }

            friend.setName(friendDTO.getName());
            friend.setBirthdate(friendDTO.getBirthdate());
            friend.setNote(friendDTO.getNote());
            friend.setEmail(friendDTO.getEmail());
            friend.setPhone(friendDTO.getPhone());

            GenderEnum gender = AppUtils.getGender(friendDTO.getGender());
            friend.setGender(gender);

        }, () -> { throw new FriendNotFoundException(MessageFormat.format(AppConstant.FRIEND_NOT_FOUND, username)); });
    }

    @Override
    public void deleteFriendBelongToUser(Long id, String username) {
        Optional<Friend> friendOptional = friendRepository.findFriendByIdAndUserEmail(id, username);

        friendOptional.ifPresentOrElse(friendRepository::delete, () -> {
            throw new FriendNotFoundException(MessageFormat.format(AppConstant.FRIEND_NOT_FOUND, username));
        });
    }

    @Override
    public PageDTO<FriendDTO> getFriendsBelongToUser(String username, Pageable pageable) {
        Page<Friend> pageFriends = friendRepository.getFriendsBelongToUser(username, pageable);
        List<FriendDTO> friendDTOs = pageFriends.stream()
                .map(FriendDTO::from)
                .collect(Collectors.toList());

        PageDTO<FriendDTO> pageFriendDTOs =  new PageDTO<>();
        pageFriendDTOs.setData(friendDTOs);
        pageFriendDTOs.setCurrentPage(pageFriends.getNumber());
        pageFriendDTOs.setTotalPages(pageFriends.getTotalPages());
        pageFriendDTOs.setTotalItems(pageFriends.getTotalElements());

        return pageFriendDTOs;
    }

    @Override
    public FriendDTO getFriendBelongToUser(Long id, String username) {
        Optional<Friend> optionalFriend = friendRepository.findFriendByIdAndUserEmail(id, username);

        return optionalFriend.map(FriendDTO::from)
                .orElseThrow(() -> new FriendNotFoundException(
                        MessageFormat.format(AppConstant.FRIEND_NOT_FOUND, username)));
    }
}
