package com.gnoulel.birthdayforfriends.repository;

import com.gnoulel.birthdayforfriends.entity.Friend;
import com.gnoulel.birthdayforfriends.repository.customquery.FriendCustomQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {

    boolean isEmailExistedBelongToUser(@Param("username") String username, @Param("email") String email);
    boolean isPhoneExistedBelongToUser(@Param("username") String username, @Param("phone") String phone);
    @Query(FriendCustomQuery.GET_FRIENDS_BELONG_TO_USER)
    Page<Friend> getFriendsBelongToUser(@Param("username") String username, Pageable pageable);
    Optional<Friend> findFriendByIdAndUserEmail(Long id, String email);
}
