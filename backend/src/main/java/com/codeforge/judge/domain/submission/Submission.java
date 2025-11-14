package com.codeforge.judge.domain.submission;

import com.codeforge.judge.domain.AppUser;
import com.codeforge.judge.domain.BaseEntity;
import com.codeforge.judge.domain.Problem;
import com.codeforge.judge.domain.ProgrammingLanguage;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "submission")
public class Submission extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id")
    private Problem problem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private AppUser user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "language_id")
    private ProgrammingLanguage language;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private SubmissionStatus status = SubmissionStatus.QUEUED;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private SubmissionVerdict verdict = SubmissionVerdict.INTERNAL_ERROR;

    @Column(nullable = false)
    private int score;

    @Lob
    @Column(nullable = false)
    private String code;

    @OneToMany(mappedBy = "submission")
    private Set<SubmissionTestcaseResult> testcaseResults = new HashSet<>();

    public Problem getProblem() {
        return problem;
    }

    public void setProblem(Problem problem) {
        this.problem = problem;
    }

    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }

    public ProgrammingLanguage getLanguage() {
        return language;
    }

    public void setLanguage(ProgrammingLanguage language) {
        this.language = language;
    }

    public SubmissionStatus getStatus() {
        return status;
    }

    public void setStatus(SubmissionStatus status) {
        this.status = status;
    }

    public SubmissionVerdict getVerdict() {
        return verdict;
    }

    public void setVerdict(SubmissionVerdict verdict) {
        this.verdict = verdict;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Set<SubmissionTestcaseResult> getTestcaseResults() {
        return testcaseResults;
    }

    public void setTestcaseResults(Set<SubmissionTestcaseResult> testcaseResults) {
        this.testcaseResults = testcaseResults;
    }
}

