package com.codeforge.judge.domain.submission;

import com.codeforge.judge.domain.BaseEntity;
import com.codeforge.judge.domain.ProblemTestcase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "submission_testcase_result")
public class SubmissionTestcaseResult extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submission_id")
    private Submission submission;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "testcase_id")
    private ProblemTestcase testcase;

    @Column(name = "time_ms")
    private Integer timeMs;

    @Column(name = "memory_kb")
    private Integer memoryKb;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private SubmissionVerdict verdict;

    @Column(columnDefinition = "TEXT")
    private String message;

    public Submission getSubmission() {
        return submission;
    }

    public void setSubmission(Submission submission) {
        this.submission = submission;
    }

    public ProblemTestcase getTestcase() {
        return testcase;
    }

    public void setTestcase(ProblemTestcase testcase) {
        this.testcase = testcase;
    }

    public Integer getTimeMs() {
        return timeMs;
    }

    public void setTimeMs(Integer timeMs) {
        this.timeMs = timeMs;
    }

    public Integer getMemoryKb() {
        return memoryKb;
    }

    public void setMemoryKb(Integer memoryKb) {
        this.memoryKb = memoryKb;
    }

    public SubmissionVerdict getVerdict() {
        return verdict;
    }

    public void setVerdict(SubmissionVerdict verdict) {
        this.verdict = verdict;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

