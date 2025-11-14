package com.codeforge.judge.service.mapper;

import com.codeforge.judge.domain.AppUser;
import com.codeforge.judge.service.dto.UserDTO;
import java.util.Set;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-12T15:43:14+0700",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.44.0.v20251023-0518, environment: Java 21.0.8 (Eclipse Adoptium)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDTO toDto(AppUser user) {
        if ( user == null ) {
            return null;
        }

        UUID id = null;
        String username = null;
        String email = null;
        String displayName = null;

        id = user.getId();
        username = user.getUsername();
        email = user.getEmail();
        displayName = user.getDisplayName();

        Set<String> roles = mapRoles(user);

        UserDTO userDTO = new UserDTO( id, username, email, displayName, roles );

        return userDTO;
    }
}
