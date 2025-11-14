package com.codeforge.judge.service.dto;

import com.codeforge.judge.domain.Difficulty;

import java.time.Instant;
import java.util.UUID;

public record ProblemSummaryDTO(
        UUID id,
        String slug,
        String title,
        Difficulty difficulty,
        String tags,
        Instant updatedAt
) {
}

