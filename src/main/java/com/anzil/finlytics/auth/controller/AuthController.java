package com.anzil.finlytics.auth.controller;

import com.anzil.finlytics.auth.dto.*;
import com.anzil.finlytics.auth.service.AuthService;
import com.anzil.finlytics.auth.service.RefreshTokenService;
import com.anzil.finlytics.security.CookieUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Auth APIs")
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final CookieUtils cookieUtils;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(
            @Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest request,
            HttpServletResponse response) {

        AuthResult result = authService.loginWithRefresh(request);

        cookieUtils.addRefreshToken(response, result.getRefreshToken());

        return ResponseEntity.ok(
                new LoginResponse(
                        result.getAccessToken(),
                        result.getEmail(),
                        result.getRole()
                )
        );
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(
            @CookieValue(name = "refreshToken", required = false) String token) {

        if (token == null) {
            throw new RuntimeException("No refresh token cookie");
        }

        return refreshTokenService.findByToken(token)
                .map(refreshTokenService::verifyExpiration)
                .map(rt -> rt.getUser())
                .map(user -> {
                    String newAccessToken = authService.generateAccessToken(user);

                    return ResponseEntity.ok(
                            new LoginResponse(
                                    newAccessToken,
                                    user.getEmail(),
                                    user.getRole().name()
                            )
                    );
                })
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));
    }


    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            HttpServletResponse response,
            @CookieValue(name = "refreshToken", required = false) String token) {

        if (token != null) {
            refreshTokenService.findByToken(token)
                    .ifPresent(rt -> refreshTokenService.deleteByUser(rt.getUser()));
        }

        cookieUtils.clear(response);

        return ResponseEntity.ok("Logged out");
    }
}