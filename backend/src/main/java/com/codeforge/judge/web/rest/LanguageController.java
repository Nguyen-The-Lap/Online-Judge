package com.codeforge.judge.web.rest;

import com.codeforge.judge.service.ProgrammingLanguageService;
import com.codeforge.judge.service.dto.ProgrammingLanguageDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/languages")
public class LanguageController {

    private final ProgrammingLanguageService languageService;

    public LanguageController(ProgrammingLanguageService languageService) {
        this.languageService = languageService;
    }

    @GetMapping
    public List<ProgrammingLanguageDTO> list() {
        return languageService.listLanguages();
    }
}

