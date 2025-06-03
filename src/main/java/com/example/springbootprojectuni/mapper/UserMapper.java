package com.example.springbootprojectuni.mapper;

import com.example.springbootprojectuni.dto.RegisterRequest;
import com.example.springbootprojectuni.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(RegisterRequest registerRequest) {
        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setPassword(registerRequest.getPassword());
        user.setName(registerRequest.getName());
        user.setRole(registerRequest.getRole());
        return user;
    }
}
