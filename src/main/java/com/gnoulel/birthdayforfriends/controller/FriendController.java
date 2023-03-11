package com.gnoulel.birthdayforfriends.controller;

import com.gnoulel.birthdayforfriends.dto.FriendDTO;
import com.gnoulel.birthdayforfriends.dto.PageDTO;
import com.gnoulel.birthdayforfriends.exception.UserNotFoundException;
import com.gnoulel.birthdayforfriends.service.FriendService;
import com.gnoulel.birthdayforfriends.utils.AppUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
public class FriendController {
    private static final String TAG = "FriendController | ";

    private final FriendService friendService;

    public FriendController(FriendService friendService) {
        this.friendService = friendService;
    }

    @PostMapping("${api.friends-management.url}")
    @ResponseStatus(HttpStatus.CREATED)
    public FriendDTO addFriendToUser(@RequestBody FriendDTO friendDTO) {
        String loggedUsername = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (StringUtils.isEmpty(loggedUsername)) {
            throw new UserNotFoundException("Can't get authenticated user");
        }

        return friendService.addFriendToUser(loggedUsername, friendDTO);
    }

    @PutMapping("${api.friends-management.url}")
    @ResponseStatus(HttpStatus.OK)
    public void updateFriendBelongToUser(@RequestBody FriendDTO friendDTO) {
        String loggedUsername = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (StringUtils.isEmpty(loggedUsername)) {
            throw new UserNotFoundException("Can't get authenticated user");
        }

        friendService.updateFriendBelongToUser(friendDTO, loggedUsername);
    }

    @DeleteMapping("${api.friends-management.delete.url}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteFriendBelongToUser(@PathVariable("id") long id) {
        String loggedUsername = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (StringUtils.isEmpty(loggedUsername)) {
            throw new UserNotFoundException("Can't get authenticated user");
        }

        friendService.deleteFriendBelongToUser(id, loggedUsername);
    }

    @GetMapping("${api.friends-management.url}")
    @ResponseStatus(HttpStatus.OK)
    public PageDTO<FriendDTO> getFriendsBelongToUser(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sort", defaultValue = "id,desc") String[] sorts) {
        String loggedUsername = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (StringUtils.isEmpty(loggedUsername)) {
            throw new UserNotFoundException("Can't get authenticated user");
        }

        List<Sort.Order> orders = new ArrayList<>();
        try {
            if (StringUtils.contains(sorts[0], ",")) {
                // will sort more than 2 fields, sortOrder="field, direction"
                for (String sort : sorts) {
                    String[] eachSort = StringUtils.split(sort, ",");

                    orders.add(new Sort.Order(AppUtils.getDirection(eachSort[1]), eachSort[0]));
                }
            } else {
                // will sort only one field, sort=[field, direction]
                orders.add(new Sort.Order(AppUtils.getDirection(sorts[1]), sorts[0]));
            }
        } catch (Exception e) {
            log.error(TAG + "Error while parsing sort params");
            if (orders.size() == 0) {
                orders.add(new Sort.Order(Sort.Direction.DESC, "id"));
            }
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(orders));
        return friendService.getFriendsBelongToUser(loggedUsername, pageable);
    }

    @GetMapping("${api.friends-management.get.url}")
    @ResponseStatus(HttpStatus.OK)
    public FriendDTO getFriendBelongToUser(@PathVariable("id") long id) {
        String loggedUsername = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (StringUtils.isEmpty(loggedUsername)) {
            throw new UserNotFoundException("Can't get authenticated user");
        }

        return friendService.getFriendBelongToUser(id, loggedUsername);
    }

}
