package com.codeforge.judge.service.mapper;

import com.codeforge.judge.domain.Difficulty;
import com.codeforge.judge.domain.Problem;
import com.codeforge.judge.service.dto.ProblemDetailDTO;
import com.codeforge.judge.service.dto.ProblemSummaryDTO;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-14T17:06:54+0700",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.44.0.v20251023-0518, environment: Java 21.0.8 (Eclipse Adoptium)"
)
@Component
public class ProblemMapperImpl implements ProblemMapper {

    @Override
    public ProblemSummaryDTO toSummary(Problem problem) {
        if ( problem == null ) {
            return null;
        }

        UUID id = null;
        String slug = null;
        String title = null;
        Difficulty difficulty = null;
        String tags = null;
        Instant updatedAt = null;

        id = problem.getId();
        slug = problem.getSlug();
        title = problem.getTitle();
        difficulty = problem.getDifficulty();
        tags = problem.getTags();
        updatedAt = problem.getUpdatedAt();

        ProblemSummaryDTO problemSummaryDTO = new ProblemSummaryDTO( id, slug, title, difficulty, tags, updatedAt );

        return problemSummaryDTO;
    }

    @Override
    public ProblemDetailDTO toDetail(Problem problem) {
        if ( problem == null ) {
            return null;
        }

        UUID id = null;
        String slug = null;
        String title = null;
        Difficulty difficulty = null;
        String tags = null;
        String statement = null;
        String inputSpecification = null;
        String outputSpecification = null;
        String sampleInput = null;
        String sampleOutput = null;
        int timeLimitMs = 0;
        int memoryLimitMb = 0;

        id = problem.getId();
        slug = problem.getSlug();
        title = problem.getTitle();
        difficulty = problem.getDifficulty();
        tags = problem.getTags();
        statement = problem.getStatement();
        inputSpecification = problem.getInputSpecification();
        outputSpecification = problem.getOutputSpecification();
        sampleInput = problem.getSampleInput();
        sampleOutput = problem.getSampleOutput();
        timeLimitMs = problem.getTimeLimitMs();
        memoryLimitMb = problem.getMemoryLimitMb();

        List<ProblemDetailDTO.TestcaseDTO> testcases = toTestcaseDto(problem.getTestcases());

        ProblemDetailDTO problemDetailDTO = new ProblemDetailDTO( id, slug, title, difficulty, tags, statement, inputSpecification, outputSpecification, sampleInput, sampleOutput, timeLimitMs, memoryLimitMb, testcases );

        return problemDetailDTO;
    }
}
