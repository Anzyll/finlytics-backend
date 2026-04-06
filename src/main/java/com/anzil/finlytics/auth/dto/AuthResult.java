package com.anzil.finlytics.auth.dto;

public class AuthResult {

    private final String accessToken;
    private final String refreshToken;
    private final String email;
    private final String role;

    public AuthResult(String accessToken, String refreshToken, String email, String role) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.email = email;
        this.role = role;
    }

    public String getAccessToken() { return accessToken; }
    public String getRefreshToken() { return refreshToken; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
}