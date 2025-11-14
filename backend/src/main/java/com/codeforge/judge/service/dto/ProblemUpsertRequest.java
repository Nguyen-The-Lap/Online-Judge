package com.codeforge.judge.service.dto;

import com.codeforge.judge.domain.Difficulty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record ProblemUpsertRequest(
        @NotBlank @Size(max = 80) String slug,
        @NotBlank @Size(max = 150) String title,
        @NotNull Difficulty difficulty,
        String tags,
        @NotBlank String statement,
        String inputSpecification,
        String outputSpecification,
        String sampleInput,
        String sampleOutput,
        Integer timeLimitMs,
        Integer memoryLimitMb,
        List<TestcasePayload> testcases
) {
    public record TestcasePayload(
            @NotBlank String inputBlob,
            @NotBlank String outputBlob,
            boolean visible,
            int score
    ) {
    }
}

