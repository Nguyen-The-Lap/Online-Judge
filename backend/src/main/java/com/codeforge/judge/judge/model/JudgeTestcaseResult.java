package com.codeforge.judge.judge.model;

import com.codeforge.judge.domain.submission.SubmissionVerdict;

import java.util.UUID;

public record JudgeTestcaseResult(
        UUID testcaseId,
        SubmissionVerdict verdict,
        Integer timeMs,
        Integer memoryKb,
        String message
) {
}

