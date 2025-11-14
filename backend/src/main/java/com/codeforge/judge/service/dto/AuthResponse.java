package com.codeforge.judge.service.dto;

public record AuthResponse(
        String token,
        UserDTO user
) {
}

