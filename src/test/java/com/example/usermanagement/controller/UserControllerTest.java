package com.example.usermanagement.controller;

import com.example.usermanagement.model.User;
import com.example.usermanagement.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@Import(com.example.usermanagement.config.SecurityConfig.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    void getUserById_WhenUserExists_ReturnsUser() throws Exception {
        // Given
        User user = new User("田中太郎", "tanaka@example.com", "090-1234-5678", "開発部");
        user.setId(1L);
        when(userService.getUserById(1L)).thenReturn(Optional.of(user));

        // When & Then
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("田中太郎"))
                .andExpect(jsonPath("$.email").value("tanaka@example.com"))
                .andExpect(jsonPath("$.phone").value("090-1234-5678"))
                .andExpect(jsonPath("$.department").value("開発部"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getUserById_WhenUserNotExists_ReturnsNotFound() throws Exception {
        // Given
        when(userService.getUserById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/users/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getUserById_WhenNotAuthenticated_ReturnsUnauthorized() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    void getUserById_WhenNotAdmin_ReturnsForbidden() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isForbidden());
    }
}