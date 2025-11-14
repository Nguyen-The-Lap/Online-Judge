package com.codeforge.judge.repository;

import com.codeforge.judge.domain.ProgrammingLanguage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProgrammingLanguageRepository extends JpaRepository<ProgrammingLanguage, UUID> {
    Optional<ProgrammingLanguage> findByName(String name);
}

