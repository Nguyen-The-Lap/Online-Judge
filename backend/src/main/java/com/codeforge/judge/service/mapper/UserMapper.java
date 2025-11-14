package com.codeforge.judge.service.mapper;

import com.codeforge.judge.domain.AppUser;
import com.codeforge.judge.service.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "roles", expression = "java(mapRoles(user))")
    UserDTO toDto(AppUser user);

    default Set<String> mapRoles(AppUser user) {
        return user.getRoles()
                .stream()
                .map(Enum::name)
                .collect(Collectors.toSet());
    }
}

