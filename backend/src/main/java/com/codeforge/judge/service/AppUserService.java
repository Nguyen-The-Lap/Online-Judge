package com.codeforge.judge.service;

import com.codeforge.judge.domain.AppUser;
import com.codeforge.judge.domain.Role;
import com.codeforge.judge.repository.AppUserRepository;
import com.codeforge.judge.service.dto.RegisterRequest;
import com.codeforge.judge.service.dto.UserDTO;
import com.codeforge.judge.service.mapper.UserMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Service
public class AppUserService {

    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public AppUserService(AppUserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    public Optional<AppUser> findByUsernameOrEmail(String login) {
        return userRepository.findByUsername(login).or(() -> userRepository.findByEmail(login));
    }

    @Transactional
    public UserDTO register(RegisterRequest request, boolean admin) {
        userRepository.findByUsername(request.username())
                .ifPresent(user -> {
                    throw new org.springframework.web.server.ResponseStatusException(
                            org.springframework.http.HttpStatus.CONFLICT, "Username đã tồn tại");
                });
        userRepository.findByEmail(request.email())
                .ifPresent(user -> {
                    throw new org.springframework.web.server.ResponseStatusException(
                            org.springframework.http.HttpStatus.CONFLICT, "Email đã tồn tại");
                });
        AppUser user = new AppUser();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setDisplayName(request.displayName());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setRoles(admin ? Set.of(Role.ADMIN, Role.USER) : Set.of(Role.USER));
        AppUser saved = userRepository.save(user);
        return userMapper.toDto(saved);
    }

    public UserDTO toDto(AppUser user) {
        return userMapper.toDto(user);
    }
}

