package com.codeforge.judge.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "problem_testcase")
public class ProblemTestcase extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id")
    private Problem problem;

    @Column(name = "input_blob", columnDefinition = "TEXT", nullable = false)
    private String inputBlob;

    @Column(name = "output_blob", columnDefinition = "TEXT", nullable = false)
    private String outputBlob;

    @Column(nullable = false)
    private boolean visible;

    @Column(nullable = false)
    private int score = 100;

    public Problem getProblem() {
        return problem;
    }

    public void setProblem(Problem problem) {
        this.problem = problem;
    }

    public String getInputBlob() {
        return inputBlob;
    }

    public void setInputBlob(String inputBlob) {
        this.inputBlob = inputBlob;
    }

    public String getOutputBlob() {
        return outputBlob;
    }

    public void setOutputBlob(String outputBlob) {
        this.outputBlob = outputBlob;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}

