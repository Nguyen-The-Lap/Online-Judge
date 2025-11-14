package com.codeforge.judge.judge;

import com.codeforge.judge.domain.ProblemTestcase;
import com.codeforge.judge.domain.submission.Submission;
import com.codeforge.judge.domain.submission.SubmissionStatus;
import com.codeforge.judge.domain.submission.SubmissionTestcaseResult;
import com.codeforge.judge.judge.executor.JudgeExecutor;
import com.codeforge.judge.judge.model.JudgeOutcome;
import com.codeforge.judge.judge.model.JudgeTestcaseResult;
import com.codeforge.judge.repository.ProblemTestcaseRepository;
import com.codeforge.judge.repository.SubmissionRepository;
import com.codeforge.judge.repository.SubmissionTestcaseResultRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Service
public class JudgeService {

    private static final Logger log = LoggerFactory.getLogger(JudgeService.class);

    private final SubmissionRepository submissionRepository;
    private final SubmissionTestcaseResultRepository testcaseResultRepository;
    private final ProblemTestcaseRepository problemTestcaseRepository;
    private final JudgeExecutor judgeExecutor;
    private final Executor judgeExecutorPool;
    private final TransactionTemplate transactionTemplate;

    public JudgeService(SubmissionRepository submissionRepository,
                        SubmissionTestcaseResultRepository testcaseResultRepository,
                        ProblemTestcaseRepository problemTestcaseRepository,
                        JudgeExecutor judgeExecutor,
                        @Qualifier("judgeExecutor") Executor judgeExecutorPool,
                        PlatformTransactionManager transactionManager) {
        this.submissionRepository = submissionRepository;
        this.testcaseResultRepository = testcaseResultRepository;
        this.problemTestcaseRepository = problemTestcaseRepository;
        this.judgeExecutor = judgeExecutor;
        this.judgeExecutorPool = judgeExecutorPool;
        this.transactionTemplate = new TransactionTemplate(transactionManager);
    }

    public void enqueue(Submission submission) {
        CompletableFuture.runAsync(() -> transactionTemplate.execute(status -> {
            runJudge(submission.getId());
            return null;
        }), judgeExecutorPool);
    }

    private void runJudge(UUID submissionId) {
        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new IllegalStateException("Submission biến mất trong quá trình xử lý"));
        List<ProblemTestcase> testcases = problemTestcaseRepository.findByProblem(submission.getProblem());
        submission.setStatus(SubmissionStatus.RUNNING);
        submissionRepository.save(submission);
        try {
            JudgeOutcome outcome = judgeExecutor.execute(submission, testcases);
            submission.setStatus(SubmissionStatus.COMPLETED);
            submission.setVerdict(outcome.verdict());
            submission.setScore(outcome.score());
            submissionRepository.save(submission);

            testcaseResultRepository.deleteAll(testcaseResultRepository.findBySubmission(submission));

            for (JudgeTestcaseResult result : outcome.testcaseResults()) {
                SubmissionTestcaseResult entity = new SubmissionTestcaseResult();
                entity.setSubmission(submission);
                ProblemTestcase testcase = testcases.stream()
                        .filter(tc -> tc.getId().equals(result.testcaseId()))
                        .findFirst()
                        .orElse(null);
                if (testcase != null) {
                    entity.setTestcase(testcase);
                }
                entity.setVerdict(result.verdict());
                entity.setTimeMs(result.timeMs());
                entity.setMemoryKb(result.memoryKb());
                entity.setMessage(result.message());
                testcaseResultRepository.save(entity);
            }
        } catch (Exception ex) {
            log.error("Lỗi khi chấm submission {}", submissionId, ex);
            submission.setStatus(SubmissionStatus.ERRORED);
            submissionRepository.save(submission);
        }
    }
}

