package com.anzil.finlytics.auth.service;

import com.anzil.finlytics.auth.dto.*;
import com.anzil.finlytics.auth.entity.RefreshToken;
import com.anzil.finlytics.auth.mapper.UserMapper;
import com.anzil.finlytics.common.exception.AppException;
import com.anzil.finlytics.common.exception.ErrorCode;
import com.anzil.finlytics.security.JwtUtil;
import com.anzil.finlytics.user.entity.User;
import com.anzil.finlytics.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    public RegisterResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.email())) {
            throw new AppException(ErrorCode.EMAIL_ALREADY_EXISTS);
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
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_CREDENTIALS));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new AppException(ErrorCode.INVALID_CREDENTIALS);
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());

        return new LoginResponse(
                token,
                user.getEmail(),
                user.getRole().name()
        );
    }
    public AuthResult loginWithRefresh(LoginRequest request) {

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_CREDENTIALS));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new AppException(ErrorCode.INVALID_CREDENTIALS);
        }

        String accessToken = jwtUtil.generateToken(user.getEmail(), user.getRole().name());


        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());


        return new AuthResult(
                accessToken,
                refreshToken.getToken(),
                user.getEmail(),
                user.getRole().name()
        );
    }
    public String generateAccessToken(User user) {
        return jwtUtil.generateToken(user.getEmail(), user.getRole().name());
    }
}