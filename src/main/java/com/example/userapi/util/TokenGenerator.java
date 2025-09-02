package com.example.userapi.util;

import com.example.userapi.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("token-gen")
public class TokenGenerator implements CommandLineRunner {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public void run(String... args) throws Exception {
        String adminToken = jwtUtil.generateToken("admin@example.com", "ADMIN");
        String userToken = jwtUtil.generateToken("user@example.com", "USER");
        
        System.out.println("Admin Token:");
        System.out.println(adminToken);
        System.out.println();
        System.out.println("User Token:");
        System.out.println(userToken);
    }
}