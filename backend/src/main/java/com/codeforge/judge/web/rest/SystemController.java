package com.codeforge.judge.web.rest;

import com.codeforge.judge.repository.AppUserRepository;
import com.codeforge.judge.repository.ProblemRepository;
import com.codeforge.judge.repository.SubmissionRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/system")
public class SystemController {

    private final AppUserRepository userRepository;
    private final ProblemRepository problemRepository;
    private final SubmissionRepository submissionRepository;

    public SystemController(AppUserRepository userRepository,
                            ProblemRepository problemRepository,
                            SubmissionRepository submissionRepository) {
        this.userRepository = userRepository;
        this.problemRepository = problemRepository;
        this.submissionRepository = submissionRepository;
    }

    @GetMapping("/stats")
    public Map<String, Object> stats() {
        return Map.of(
                "users", userRepository.count(),
                "problems", problemRepository.count(),
                "submissions", submissionRepository.count()
        );
    }
}

