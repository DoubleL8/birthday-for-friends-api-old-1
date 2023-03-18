package com.gnoulel.birthdayforfriends.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gnoulel.birthdayforfriends.dto.UserDTO;
import com.gnoulel.birthdayforfriends.enums.RoleEnum;
import com.gnoulel.birthdayforfriends.service.UserService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    private MockMvc mockMvc;
    @InjectMocks
    UserController userController;
    @Mock
    UserService userService;

    private String signupUrl = "/api/auth/signup";

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .addPlaceholderValue("api.auth.signup.url", signupUrl)
                .build();
    }

    @Test
    public void testSignupSuccess() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setName("Name Test");
        userDTO.setEmail("test@test.test");
        userDTO.setPassword("password");

        UserDTO saveDTO = new UserDTO();
        saveDTO.setId(1L);
        saveDTO.setName("Name Test");
        saveDTO.setEmail("test@test.test");
        saveDTO.setRole(RoleEnum.USER_ROLE.name());

        Mockito.when(userService.save(userDTO)).thenReturn(saveDTO);

        mockMvc.perform(post(signupUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.id", Matchers.is(1)));

    }
}
