package com.example.userservice.mapper;

import com.example.userservice.dto.RegisterRequest;
import com.example.userservice.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(RegisterRequest request) {
        return User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(request.getPassword())
                .role("USER")
                .build();
    }
}
