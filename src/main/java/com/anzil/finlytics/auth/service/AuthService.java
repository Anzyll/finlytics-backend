package com.anzil.finlytics.auth.service;

import com.anzil.finlytics.auth.dto.LoginRequest;
import com.anzil.finlytics.auth.dto.LoginResponse;
import com.anzil.finlytics.auth.dto.RegisterRequest;
import com.anzil.finlytics.auth.dto.RegisterResponse;
import com.anzil.finlytics.auth.mapper.UserMapper;
import com.anzil.finlytics.security.JwtUtil;
import com.anzil.finlytics.user.entity.User;
import com.anzil.finlytics.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public RegisterResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.email())) {
            throw new RuntimeException("Email already exists");
        }

        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.password()));

        User saved = userRepository.save(user);

        return new RegisterResponse(
                saved.getId(),
                saved.getEmail(),
                "User registered successfully"
        );
    }

    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());

        return new LoginResponse(
                token,
                user.getEmail(),
                user.getRole().name()
        );
}
}