package com.codeforge.judge.service.dto;

import com.codeforge.judge.domain.submission.SubmissionStatus;
import com.codeforge.judge.domain.submission.SubmissionVerdict;

import java.util.List;
import java.util.UUID;

public record SubmissionDetailDTO(
        UUID id,
        UUID problemId,
        SubmissionStatus status,
        SubmissionVerdict verdict,
        int score,
        String language,
        String code,
        List<TestcaseResultDTO> testcaseResults
) {
    public record TestcaseResultDTO(
            UUID testcaseId,
            SubmissionVerdict verdict,
            Integer timeMs,
            Integer memoryKb,
            String message
    ) {
    }
}

