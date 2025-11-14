package com.codeforge.judge.service;

import com.codeforge.judge.domain.AppUser;
import com.codeforge.judge.domain.Problem;
import com.codeforge.judge.domain.ProgrammingLanguage;
import com.codeforge.judge.domain.Role;
import com.codeforge.judge.domain.submission.Submission;
import com.codeforge.judge.domain.submission.SubmissionStatus;
import com.codeforge.judge.domain.submission.SubmissionVerdict;
import com.codeforge.judge.repository.ProblemRepository;
import com.codeforge.judge.repository.ProgrammingLanguageRepository;
import com.codeforge.judge.repository.SubmissionRepository;
import com.codeforge.judge.repository.SubmissionTestcaseResultRepository;
import com.codeforge.judge.service.dto.SubmissionCreateRequest;
import com.codeforge.judge.service.dto.SubmissionDTO;
import com.codeforge.judge.service.dto.SubmissionDetailDTO;
import com.codeforge.judge.service.mapper.SubmissionMapper;
import com.codeforge.judge.judge.JudgeService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class SubmissionService {

    private final SubmissionRepository submissionRepository;
    private final ProgrammingLanguageRepository languageRepository;
    private final ProblemRepository problemRepository;
    private final SubmissionTestcaseResultRepository testcaseResultRepository;
    private final SubmissionMapper submissionMapper;
    private final JudgeService judgeService;

    public SubmissionService(SubmissionRepository submissionRepository,
                             ProgrammingLanguageRepository languageRepository,
                             ProblemRepository problemRepository,
                             SubmissionTestcaseResultRepository testcaseResultRepository,
                             SubmissionMapper submissionMapper,
                             JudgeService judgeService) {
        this.submissionRepository = submissionRepository;
        this.languageRepository = languageRepository;
        this.problemRepository = problemRepository;
        this.testcaseResultRepository = testcaseResultRepository;
        this.submissionMapper = submissionMapper;
        this.judgeService = judgeService;
    }

    @Transactional
    public SubmissionDTO createSubmission(AppUser user, SubmissionCreateRequest request) {
        Problem problem = problemRepository.findById(request.problemId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy bài toán"));
        ProgrammingLanguage language = languageRepository.findById(request.languageId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy ngôn ngữ"));
        if (!language.isEnableSubmission()) {
            throw new IllegalStateException("Ngôn ngữ đã bị vô hiệu hóa");
        }

        Submission submission = new Submission();
        submission.setProblem(problem);
        submission.setLanguage(language);
        submission.setUser(user);
        submission.setCode(request.sourceCode());
        submission.setStatus(SubmissionStatus.QUEUED);
        submission.setVerdict(SubmissionVerdict.INTERNAL_ERROR);
        submission.setScore(0);

        Submission saved = submissionRepository.save(submission);
        judgeService.enqueue(saved);
        return submissionMapper.toDto(saved);
    }

    @Transactional(readOnly = true)
    public SubmissionDetailDTO getSubmission(UUID submissionId, AppUser requester) {
        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy submission"));
        boolean isOwner = submission.getUser().getId().equals(requester.getId());
        boolean isAdmin = requester.getRoles().contains(Role.ADMIN);
        if (!isOwner && !isAdmin) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.FORBIDDEN,
                    "Bạn không có quyền xem bài nộp này");
        }
        submission.setTestcaseResults(testcaseResultRepository.findBySubmission(submission).stream()
                .collect(java.util.stream.Collectors.toCollection(java.util.HashSet::new)));
        return submissionMapper.toDetail(submission);
    }

    public List<SubmissionDTO> listByProblem(UUID problemId) {
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy bài toán"));
        return submissionRepository.findByProblemOrderByCreatedAtDesc(problem).stream()
                .map(submissionMapper::toDto)
                .toList();
    }

    public List<SubmissionDTO> listForUser(AppUser user) {
        return submissionRepository.findByUserOrderByCreatedAtDesc(user).stream()
                .map(submissionMapper::toDto)
                .toList();
    }
}

