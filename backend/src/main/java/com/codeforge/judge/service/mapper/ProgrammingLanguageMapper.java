package com.codeforge.judge.service.mapper;

import com.codeforge.judge.domain.ProgrammingLanguage;
import com.codeforge.judge.service.dto.ProgrammingLanguageDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProgrammingLanguageMapper {

    ProgrammingLanguageDTO toDto(ProgrammingLanguage language);
}

