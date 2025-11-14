package com.codeforge.judge.service.dto;

import com.codeforge.judge.domain.submission.SubmissionStatus;
import com.codeforge.judge.domain.submission.SubmissionVerdict;

import java.time.Instant;
import java.util.UUID;

public record SubmissionDTO(
        UUID id,
        UUID problemId,
        UUID languageId,
        SubmissionStatus status,
        SubmissionVerdict verdict,
        int score,
        Instant createdAt
) {
}

