package com.codeforge.judge.repository;

import com.codeforge.judge.domain.AppUser;
import com.codeforge.judge.domain.Problem;
import com.codeforge.judge.domain.submission.Submission;
import com.codeforge.judge.domain.submission.SubmissionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SubmissionRepository extends JpaRepository<Submission, UUID> {
    List<Submission> findByProblemOrderByCreatedAtDesc(Problem problem);

    List<Submission> findByUserOrderByCreatedAtDesc(AppUser user);

    List<Submission> findByStatusIn(List<SubmissionStatus> statuses);

    Optional<Submission> findFirstByStatusOrderByCreatedAtAsc(SubmissionStatus status);
}
