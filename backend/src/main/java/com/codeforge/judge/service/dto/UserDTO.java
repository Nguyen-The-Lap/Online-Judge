package com.codeforge.judge.service.dto;

import java.util.Set;
import java.util.UUID;

public record UserDTO(
        UUID id,
        String username,
        String email,
        String displayName,
        Set<String> roles
) {
}

