package com.codeforge.judge.web.rest;

import com.codeforge.judge.repository.AppUserRepository;
import com.codeforge.judge.repository.ProblemRepository;
import com.codeforge.judge.repository.SubmissionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AppUserRepository userRepository;
    private final ProblemRepository problemRepository;
    private final SubmissionRepository submissionRepository;

    public AdminController(AppUserRepository userRepository,
                           ProblemRepository problemRepository,
                           SubmissionRepository submissionRepository) {
        this.userRepository = userRepository;
        this.problemRepository = problemRepository;
        this.submissionRepository = submissionRepository;
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> stats() {
        Map<String, Object> payload = Map.of(
                "users", userRepository.count(),
                "problems", problemRepository.count(),
                "submissions", submissionRepository.count()
        );
        return ResponseEntity.ok(payload);
    }
}

