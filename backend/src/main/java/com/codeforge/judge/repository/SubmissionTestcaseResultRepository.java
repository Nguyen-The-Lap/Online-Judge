package com.codeforge.judge.repository;

import com.codeforge.judge.domain.submission.Submission;
import com.codeforge.judge.domain.submission.SubmissionTestcaseResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SubmissionTestcaseResultRepository extends JpaRepository<SubmissionTestcaseResult, UUID> {
    List<SubmissionTestcaseResult> findBySubmission(Submission submission);
}

