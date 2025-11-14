package com.codeforge.judge.service.mapper;

import com.codeforge.judge.domain.ProgrammingLanguage;
import com.codeforge.judge.service.dto.ProgrammingLanguageDTO;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-14T17:06:55+0700",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.44.0.v20251023-0518, environment: Java 21.0.8 (Eclipse Adoptium)"
)
@Component
public class ProgrammingLanguageMapperImpl implements ProgrammingLanguageMapper {

    @Override
    public ProgrammingLanguageDTO toDto(ProgrammingLanguage language) {
        if ( language == null ) {
            return null;
        }

        UUID id = null;
        String name = null;
        String version = null;
        boolean enableSubmission = false;

        id = language.getId();
        name = language.getName();
        version = language.getVersion();
        enableSubmission = language.isEnableSubmission();

        ProgrammingLanguageDTO programmingLanguageDTO = new ProgrammingLanguageDTO( id, name, version, enableSubmission );

        return programmingLanguageDTO;
    }
}
