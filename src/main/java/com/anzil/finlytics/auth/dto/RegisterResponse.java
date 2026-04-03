package com.anzil.finlytics.auth.dto;


public record RegisterResponse(
        Long userId,
        String email,
        String message

) {
}