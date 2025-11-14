package com.codeforge.judge.service.dto;

import java.util.UUID;

public record ProgrammingLanguageDTO(
        UUID id,
        String name,
        String version,
        boolean enableSubmission
) {
}

