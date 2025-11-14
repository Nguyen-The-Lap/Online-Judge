package com.codeforge.judge.judge.executor;

import com.codeforge.judge.domain.ProblemTestcase;
import com.codeforge.judge.domain.submission.Submission;
import com.codeforge.judge.judge.model.JudgeOutcome;

import java.util.List;

public interface JudgeExecutor {
    JudgeOutcome execute(Submission submission, List<ProblemTestcase> testcases);
}

