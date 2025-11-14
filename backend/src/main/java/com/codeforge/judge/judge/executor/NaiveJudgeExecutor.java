package com.codeforge.judge.judge.executor;

import com.codeforge.judge.domain.ProblemTestcase;
import com.codeforge.judge.domain.submission.Submission;
import com.codeforge.judge.domain.submission.SubmissionVerdict;
import com.codeforge.judge.judge.model.JudgeOutcome;
import com.codeforge.judge.judge.model.JudgeTestcaseResult;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class NaiveJudgeExecutor implements JudgeExecutor {

    @Override
    public JudgeOutcome execute(Submission submission, List<ProblemTestcase> testcases) {
        List<JudgeTestcaseResult> results = new ArrayList<>();
        boolean accepted = true;
        for (ProblemTestcase testcase : testcases) {
            SubmissionVerdict verdict = evaluate(submission.getCode(), testcase);
            if (verdict != SubmissionVerdict.ACCEPTED) {
                accepted = false;
            }
            results.add(new JudgeTestcaseResult(
                    testcase.getId(),
                    verdict,
                    ThreadLocalRandom.current().nextInt(10, 120),
                    ThreadLocalRandom.current().nextInt(1024, 20480),
                    verdict == SubmissionVerdict.ACCEPTED ? "OK" : "Sai kết quả mong đợi"
            ));
        }

        int totalScore = accepted ? 100 : (int) (100.0 * results.stream()
                .filter(res -> res.verdict() == SubmissionVerdict.ACCEPTED)
                .count() / Math.max(1, testcases.size()));

        return new JudgeOutcome(
                accepted ? SubmissionVerdict.ACCEPTED : SubmissionVerdict.WRONG_ANSWER,
                totalScore,
                results
        );
    }

    private SubmissionVerdict evaluate(String code, ProblemTestcase testcase) {
        String expected = testcase.getOutputBlob().trim();
        if (code.contains(expected)) {
            return SubmissionVerdict.ACCEPTED;
        }
        return SubmissionVerdict.WRONG_ANSWER;
    }
}

