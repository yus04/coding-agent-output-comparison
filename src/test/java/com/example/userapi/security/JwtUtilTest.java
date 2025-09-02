package com.example.userapi.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class JwtUtilTest {

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    void generateToken_ShouldCreateValidToken() {
        String username = "test@example.com";
        String role = "ADMIN";
        
        String token = jwtUtil.generateToken(username, role);
        
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(jwtUtil.validateToken(token));
    }

    @Test
    void getUsernameFromToken_ShouldReturnCorrectUsername() {
        String username = "test@example.com";
        String role = "ADMIN";
        String token = jwtUtil.generateToken(username, role);
        
        String extractedUsername = jwtUtil.getUsernameFromToken(token);
        
        assertEquals(username, extractedUsername);
    }

    @Test
    void getRoleFromToken_ShouldReturnCorrectRole() {
        String username = "test@example.com";
        String role = "ADMIN";
        String token = jwtUtil.generateToken(username, role);
        
        String extractedRole = jwtUtil.getRoleFromToken(token);
        
        assertEquals(role, extractedRole);
    }

    @Test
    void validateToken_WithInvalidToken_ShouldReturnFalse() {
        String invalidToken = "invalid.token.here";
        
        boolean isValid = jwtUtil.validateToken(invalidToken);
        
        assertFalse(isValid);
    }

    @Test
    void validateToken_WithEmptyToken_ShouldReturnFalse() {
        String emptyToken = "";
        
        boolean isValid = jwtUtil.validateToken(emptyToken);
        
        assertFalse(isValid);
    }
}