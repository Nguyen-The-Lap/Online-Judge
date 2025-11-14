package com.codeforge.judge.web.rest;

import com.codeforge.judge.domain.AppUser;
import com.codeforge.judge.domain.Role;
import com.codeforge.judge.service.CurrentUserService;
import com.codeforge.judge.service.ProblemService;
import com.codeforge.judge.service.dto.ProblemDetailDTO;
import com.codeforge.judge.service.dto.ProblemSummaryDTO;
import com.codeforge.judge.service.dto.ProblemUpsertRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/problems")
public class ProblemController {

    private final ProblemService problemService;
    private final CurrentUserService currentUserService;

    public ProblemController(ProblemService problemService, CurrentUserService currentUserService) {
        this.problemService = problemService;
        this.currentUserService = currentUserService;
    }

    @GetMapping
    public List<ProblemSummaryDTO> list() {
        return problemService.listAll();
    }

    @GetMapping("/{slug}")
    public ProblemDetailDTO getDetail(@PathVariable String slug) {
        return problemService.getBySlug(slug);
    }

    @PreAuthorize("hasAnyRole('ADMIN','PROBLEM_SETTER')")
    @PostMapping
    public ResponseEntity<ProblemDetailDTO> create(@Valid @RequestBody ProblemUpsertRequest request) {
        AppUser user = currentUserService.getCurrentUser();
        if (!user.getRoles().contains(Role.ADMIN) && !user.getRoles().contains(Role.PROBLEM_SETTER)) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(problemService.createProblem(user, request));
    }

    @PreAuthorize("hasAnyRole('ADMIN','PROBLEM_SETTER')")
    @PutMapping("/{slug}")
    public ResponseEntity<ProblemDetailDTO> update(@PathVariable String slug,
                                                   @Valid @RequestBody ProblemUpsertRequest request) {
        return ResponseEntity.ok(problemService.updateProblem(slug, request));
    }
}

