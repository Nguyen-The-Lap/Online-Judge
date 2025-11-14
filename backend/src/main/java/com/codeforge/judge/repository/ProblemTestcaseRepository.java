package com.codeforge.judge.repository;

import com.codeforge.judge.domain.Problem;
import com.codeforge.judge.domain.ProblemTestcase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProblemTestcaseRepository extends JpaRepository<ProblemTestcase, UUID> {
    List<ProblemTestcase> findByProblem(Problem problem);
}

