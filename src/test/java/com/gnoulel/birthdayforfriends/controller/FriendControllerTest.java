package com.gnoulel.birthdayforfriends.controller;

import com.gnoulel.birthdayforfriends.dto.FriendDTO;
import com.gnoulel.birthdayforfriends.dto.PageDTO;
import com.gnoulel.birthdayforfriends.enums.GenderEnum;
import com.gnoulel.birthdayforfriends.service.FriendService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.TestSecurityContextHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(FriendController.class)
public class FriendControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    FriendService friendService;

    @BeforeEach
    public void init() {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
        "username", null, List.of(new SimpleGrantedAuthority("USER")));
        TestSecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    public void testGetFriendsBelongToUserSuccess() throws Exception {
        FriendDTO friendDTO = new FriendDTO();
        friendDTO.setId(1L);
        friendDTO.setName("Name Test");
        friendDTO.setEmail("test@test.test");
        friendDTO.setGender(GenderEnum.MALE.name());
        friendDTO.setBirthdate(LocalDate.of(2000, 1, 1));

        PageDTO<FriendDTO> pageDTO = new PageDTO<>();
        pageDTO.setData(List.of(friendDTO));
        pageDTO.setTotalPages(1);
        pageDTO.setTotalItems(1);
        pageDTO.setCurrentPage(0);

        PageRequest pageRequest = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "id"));

        Mockito.when(friendService.getFriendsBelongToUser("username", pageRequest)).thenReturn(pageDTO);

        mockMvc.perform(get("/api/friends")
                        .queryParam("sort", "id,desc")
                        .queryParam("page", "0")
                        .queryParam("size", "5"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.data", Matchers.hasSize(1)));
    }
}
