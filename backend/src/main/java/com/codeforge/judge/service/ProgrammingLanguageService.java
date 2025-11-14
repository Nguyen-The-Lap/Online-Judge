package com.codeforge.judge.service;

import com.codeforge.judge.repository.ProgrammingLanguageRepository;
import com.codeforge.judge.service.dto.ProgrammingLanguageDTO;
import com.codeforge.judge.service.mapper.ProgrammingLanguageMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProgrammingLanguageService {

    private final ProgrammingLanguageRepository languageRepository;
    private final ProgrammingLanguageMapper languageMapper;

    public ProgrammingLanguageService(ProgrammingLanguageRepository languageRepository,
                                      ProgrammingLanguageMapper languageMapper) {
        this.languageRepository = languageRepository;
        this.languageMapper = languageMapper;
    }

    public List<ProgrammingLanguageDTO> listLanguages() {
        return languageRepository.findAll().stream()
                .map(languageMapper::toDto)
                .toList();
    }
}

