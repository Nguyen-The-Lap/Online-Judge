package com.codeforge.judge.service.mapper;

import com.codeforge.judge.domain.Problem;
import com.codeforge.judge.domain.ProblemTestcase;
import com.codeforge.judge.service.dto.ProblemDetailDTO;
import com.codeforge.judge.service.dto.ProblemSummaryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.StreamSupport;

@Mapper(componentModel = "spring")
public interface ProblemMapper {

    ProblemSummaryDTO toSummary(Problem problem);

    @Mapping(target = "testcases", expression = "java(toTestcaseDto(problem.getTestcases()))")
    ProblemDetailDTO toDetail(Problem problem);

    default List<ProblemDetailDTO.TestcaseDTO> toTestcaseDto(Iterable<ProblemTestcase> source) {
        if (source == null) {
            return List.of();
        }
        return StreamSupport.stream(source.spliterator(), false)
                .map(tc -> new ProblemDetailDTO.TestcaseDTO(
                        tc.getId(),
                        tc.isVisible(),
                        tc.getScore()
                ))
                .toList();
    }
}

