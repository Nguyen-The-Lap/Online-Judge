package com.codeforge.judge.service.mapper;

import com.codeforge.judge.domain.submission.Submission;
import com.codeforge.judge.domain.submission.SubmissionStatus;
import com.codeforge.judge.domain.submission.SubmissionVerdict;
import com.codeforge.judge.service.dto.SubmissionDTO;
import com.codeforge.judge.service.dto.SubmissionDetailDTO;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-12T15:48:52+0700",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.44.0.v20251023-0518, environment: Java 21.0.8 (Eclipse Adoptium)"
)
@Component
public class SubmissionMapperImpl implements SubmissionMapper {

    @Override
    public SubmissionDTO toDto(Submission submission) {
        if ( submission == null ) {
            return null;
        }

        UUID id = null;
        SubmissionStatus status = null;
        SubmissionVerdict verdict = null;
        int score = 0;

        id = submission.getId();
        status = submission.getStatus();
        verdict = submission.getVerdict();
        score = submission.getScore();

        UUID problemId = submission.getProblem().getId();
        UUID languageId = submission.getLanguage().getId();
        Instant createdAt = submission.getCreatedAt();

        SubmissionDTO submissionDTO = new SubmissionDTO( id, problemId, languageId, status, verdict, score, createdAt );

        return submissionDTO;
    }

    @Override
    public SubmissionDetailDTO toDetail(Submission submission) {
        if ( submission == null ) {
            return null;
        }

        UUID id = null;
        SubmissionStatus status = null;
        SubmissionVerdict verdict = null;
        int score = 0;
        String code = null;

        id = submission.getId();
        status = submission.getStatus();
        verdict = submission.getVerdict();
        score = submission.getScore();
        code = submission.getCode();

        UUID problemId = submission.getProblem().getId();
        String language = submission.getLanguage().getName();
        List<SubmissionDetailDTO.TestcaseResultDTO> testcaseResults = toResultDto(submission.getTestcaseResults());

        SubmissionDetailDTO submissionDetailDTO = new SubmissionDetailDTO( id, problemId, status, verdict, score, language, code, testcaseResults );

        return submissionDetailDTO;
    }
}
