package com.codeforge.judge.judge.model;

import com.codeforge.judge.domain.submission.SubmissionVerdict;

import java.util.List;

public record JudgeOutcome(
        SubmissionVerdict verdict,
        int score,
        List<JudgeTestcaseResult> testcaseResults
) {
}

