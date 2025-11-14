package com.codeforge.judge.service;

import com.codeforge.judge.domain.AppUser;
import com.codeforge.judge.domain.Problem;
import com.codeforge.judge.domain.ProblemTestcase;
import com.codeforge.judge.repository.ProblemRepository;
import com.codeforge.judge.service.dto.ProblemDetailDTO;
import com.codeforge.judge.service.dto.ProblemSummaryDTO;
import com.codeforge.judge.service.dto.ProblemUpsertRequest;
import com.codeforge.judge.service.mapper.ProblemMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProblemService {

    private final ProblemRepository problemRepository;
    private final ProblemMapper problemMapper;

    public ProblemService(ProblemRepository problemRepository, ProblemMapper problemMapper) {
        this.problemRepository = problemRepository;
        this.problemMapper = problemMapper;
    }

    public List<ProblemSummaryDTO> listAll() {
        return problemRepository.findAll().stream()
                .map(problemMapper::toSummary)
                .toList();
    }

    public ProblemDetailDTO getBySlug(String slug) {
        Problem problem = problemRepository.findBySlug(slug)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy bài toán: " + slug));
        return problemMapper.toDetail(problem);
    }

    @Transactional
    public ProblemDetailDTO createProblem(AppUser author, ProblemUpsertRequest request) {
        problemRepository.findBySlug(request.slug())
                .ifPresent(existing -> {
                    throw new org.springframework.web.server.ResponseStatusException(
                            org.springframework.http.HttpStatus.CONFLICT, "Slug đã tồn tại");
                });
        Problem problem = new Problem();
        applyProblemData(problem, author, request);
        return problemMapper.toDetail(problemRepository.save(problem));
    }

    @Transactional
    public ProblemDetailDTO updateProblem(String slug, ProblemUpsertRequest request) {
        Problem problem = problemRepository.findBySlug(slug)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy bài toán: " + slug));
        applyProblemData(problem, problem.getAuthor(), request);
        return problemMapper.toDetail(problemRepository.save(problem));
    }

    private void applyProblemData(Problem problem, AppUser author, ProblemUpsertRequest request) {
        problem.setSlug(request.slug());
        problem.setTitle(request.title());
        problem.setAuthor(author);
        problem.setDifficulty(request.difficulty());
        problem.setTags(request.tags());
        problem.setStatement(request.statement());
        problem.setInputSpecification(request.inputSpecification());
        problem.setOutputSpecification(request.outputSpecification());
        problem.setSampleInput(request.sampleInput());
        problem.setSampleOutput(request.sampleOutput());
        if (request.timeLimitMs() != null) {
            problem.setTimeLimitMs(request.timeLimitMs());
        }
        if (request.memoryLimitMb() != null) {
            problem.setMemoryLimitMb(request.memoryLimitMb());
        }
        problem.getTestcases().clear();
        if (request.testcases() != null) {
            for (ProblemUpsertRequest.TestcasePayload payload : request.testcases()) {
                ProblemTestcase testcase = new ProblemTestcase();
                testcase.setProblem(problem);
                testcase.setInputBlob(payload.inputBlob());
                testcase.setOutputBlob(payload.outputBlob());
                testcase.setVisible(payload.visible());
                testcase.setScore(payload.score());
                problem.getTestcases().add(testcase);
            }
        }
    }
}

