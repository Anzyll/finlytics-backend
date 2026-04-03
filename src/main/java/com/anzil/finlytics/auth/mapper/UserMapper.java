package com.anzil.finlytics.auth.mapper;

import com.anzil.finlytics.auth.dto.RegisterRequest;
import com.anzil.finlytics.user.entity.Role;
import com.anzil.finlytics.user.entity.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserMapper {

    public User toEntity(RegisterRequest request) {
        return User.builder()
                .name(request.name())
                .email(request.email())
                .role(Role.VIEWER)
                .status("ACTIVE")
                .createdAt(LocalDateTime.now())
                .build();
    }
}