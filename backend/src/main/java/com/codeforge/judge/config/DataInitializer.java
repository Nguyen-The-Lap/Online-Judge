package com.codeforge.judge.config;

import com.codeforge.judge.domain.ProgrammingLanguage;
import com.codeforge.judge.repository.ProgrammingLanguageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final ProgrammingLanguageRepository languageRepository;

    public DataInitializer(ProgrammingLanguageRepository languageRepository) {
        this.languageRepository = languageRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (languageRepository.count() == 0) {
            List<ProgrammingLanguage> defaults = List.of(
                    language("Java", "21", "javac Main.java", "java Main"),
                    language("Python", "3.11", null, "python main.py"),
                    language("C++", "20", "g++ main.cpp -O2 -std=c++20 -o main", "./main")
            );
            languageRepository.saveAll(defaults);
            log.info("Đã khởi tạo {} ngôn ngữ lập trình mặc định", defaults.size());
        }
    }

    private ProgrammingLanguage language(String name, String version, String compile, String run) {
        ProgrammingLanguage language = new ProgrammingLanguage();
        language.setName(name);
        language.setVersion(version);
        language.setCompileCommand(compile);
        language.setRunCommand(run);
        language.setEnableSubmission(true);
        return language;
    }
}

