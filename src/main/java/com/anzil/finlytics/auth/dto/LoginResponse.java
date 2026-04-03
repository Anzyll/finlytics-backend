package com.anzil.finlytics.auth.dto;

public record LoginResponse(
        String token,
        String email,
        String role

) {}
