package com.example.userapi.controller;

import com.example.userapi.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/token")
    public Map<String, String> generateToken(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String role = credentials.getOrDefault("role", "user");
        
        String token = jwtUtil.generateToken(username, role);
        return Map.of("token", token);
    }
}