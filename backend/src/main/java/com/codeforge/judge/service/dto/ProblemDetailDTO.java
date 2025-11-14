package com.codeforge.judge.service.dto;

import com.codeforge.judge.domain.Difficulty;

import java.util.List;
import java.util.UUID;

public record ProblemDetailDTO(
        UUID id,
        String slug,
        String title,
        Difficulty difficulty,
        String tags,
        String statement,
        String inputSpecification,
        String outputSpecification,
        String sampleInput,
        String sampleOutput,
        int timeLimitMs,
        int memoryLimitMb,
        List<TestcaseDTO> testcases
) {
    public record TestcaseDTO(
            UUID id,
            boolean visible,
            int score
    ) {
    }
}

