package com.example.userapi.controller;

import com.example.userapi.entity.User;
import com.example.userapi.repository.UserRepository;
import com.example.userapi.security.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
class UserControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;
    private String adminToken;
    private String userToken;
    private User testUser;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        userRepository.deleteAll();
        
        // Create test users
        testUser = new User("Test User", "test@example.com", "USER");
        testUser = userRepository.save(testUser);
        
        User adminUser = new User("Admin User", "admin@example.com", "ADMIN");
        adminUser = userRepository.save(adminUser);
        
        // Generate tokens
        adminToken = jwtUtil.generateToken("admin@example.com", "ADMIN");
        userToken = jwtUtil.generateToken("user@example.com", "USER");
    }

    @Test
    void getUserById_WithAdminToken_ShouldReturnUser() throws Exception {
        mockMvc.perform(get("/api/users/{id}", testUser.getId())
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(testUser.getId()))
                .andExpect(jsonPath("$.name").value("Test User"))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.role").value("USER"));
    }

    @Test
    void getUserById_WithNonAdminToken_ShouldReturnForbidden() throws Exception {
        mockMvc.perform(get("/api/users/{id}", testUser.getId())
                .header("Authorization", "Bearer " + userToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(403))
                .andExpect(jsonPath("$.error").value("Forbidden"));
    }

    @Test
    void getUserById_WithoutToken_ShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/api/users/{id}", testUser.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getUserById_WithInvalidToken_ShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/api/users/{id}", testUser.getId())
                .header("Authorization", "Bearer invalid-token")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getUserById_WithNonExistentUserId_ShouldReturnNotFound() throws Exception {
        Long nonExistentId = 999L;
        
        mockMvc.perform(get("/api/users/{id}", nonExistentId)
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("User not found with id: " + nonExistentId));
    }
}