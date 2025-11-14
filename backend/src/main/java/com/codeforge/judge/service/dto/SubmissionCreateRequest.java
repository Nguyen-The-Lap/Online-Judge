package com.codeforge.judge.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record SubmissionCreateRequest(
        @NotNull UUID problemId,
        @NotNull UUID languageId,
        @NotBlank String sourceCode
) {
}

