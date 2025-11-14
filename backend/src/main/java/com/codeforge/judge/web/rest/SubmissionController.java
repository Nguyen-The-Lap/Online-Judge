package com.codeforge.judge.web.rest;

import com.codeforge.judge.domain.AppUser;
import com.codeforge.judge.service.CurrentUserService;
import com.codeforge.judge.service.SubmissionService;
import com.codeforge.judge.service.dto.SubmissionCreateRequest;
import com.codeforge.judge.service.dto.SubmissionDTO;
import com.codeforge.judge.service.dto.SubmissionDetailDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/submissions")
public class SubmissionController {

    private final SubmissionService submissionService;
    private final CurrentUserService currentUserService;

    public SubmissionController(SubmissionService submissionService, CurrentUserService currentUserService) {
        this.submissionService = submissionService;
        this.currentUserService = currentUserService;
    }

    @PostMapping
    public ResponseEntity<SubmissionDTO> submit(@Valid @RequestBody SubmissionCreateRequest request) {
        AppUser currentUser = currentUserService.getCurrentUser();
        return ResponseEntity.ok(submissionService.createSubmission(currentUser, request));
    }

    @GetMapping
    public List<SubmissionDTO> mySubmissions() {
        AppUser currentUser = currentUserService.getCurrentUser();
        return submissionService.listForUser(currentUser);
    }

    @GetMapping("/{id}")
    public SubmissionDetailDTO getSubmission(@PathVariable UUID id) {
        AppUser currentUser = currentUserService.getCurrentUser();
        return submissionService.getSubmission(id, currentUser);
    }

    @PreAuthorize("hasAnyRole('ADMIN','PROBLEM_SETTER')")
    @GetMapping("/problem/{problemId}")
    public List<SubmissionDTO> byProblem(@PathVariable UUID problemId) {
        return submissionService.listByProblem(problemId);
    }
}

