package com.codeforge.judge.service.mapper;

import com.codeforge.judge.domain.submission.Submission;
import com.codeforge.judge.domain.submission.SubmissionTestcaseResult;
import com.codeforge.judge.service.dto.SubmissionDTO;
import com.codeforge.judge.service.dto.SubmissionDetailDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.StreamSupport;

@Mapper(componentModel = "spring")
public interface SubmissionMapper {

    @Mapping(target = "problemId", expression = "java(submission.getProblem().getId())")
    @Mapping(target = "languageId", expression = "java(submission.getLanguage().getId())")
    @Mapping(target = "createdAt", expression = "java(submission.getCreatedAt())")
    SubmissionDTO toDto(Submission submission);

    @Mapping(target = "problemId", expression = "java(submission.getProblem().getId())")
    @Mapping(target = "language", expression = "java(submission.getLanguage().getName())")
    @Mapping(target = "testcaseResults", expression = "java(toResultDto(submission.getTestcaseResults()))")
    SubmissionDetailDTO toDetail(Submission submission);

    default List<SubmissionDetailDTO.TestcaseResultDTO> toResultDto(Iterable<SubmissionTestcaseResult> results) {
        if (results == null) {
            return List.of();
        }
        return StreamSupport.stream(results.spliterator(), false)
                .map(res -> new SubmissionDetailDTO.TestcaseResultDTO(
                        res.getTestcase() != null ? res.getTestcase().getId() : null,
                        res.getVerdict(),
                        res.getTimeMs(),
                        res.getMemoryKb(),
                        res.getMessage()
                ))
                .toList();
    }
}

