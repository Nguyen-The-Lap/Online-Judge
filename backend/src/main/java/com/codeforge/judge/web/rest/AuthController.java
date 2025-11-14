package com.codeforge.judge.web.rest;

import com.codeforge.judge.domain.AppUser;
import com.codeforge.judge.service.AppUserService;
import com.codeforge.judge.service.dto.AuthResponse;
import com.codeforge.judge.service.dto.LoginRequest;
import com.codeforge.judge.service.dto.RegisterRequest;
import com.codeforge.judge.service.dto.UserDTO;
import com.codeforge.judge.web.security.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AppUserService appUserService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthController(AppUserService appUserService,
                          AuthenticationManager authenticationManager,
                          JwtService jwtService) {
        this.appUserService = appUserService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        UserDTO user = appUserService.register(request, false);
        String token = jwtService.generateToken(user.username(), Map.of("roles", user.roles()));
        return ResponseEntity.ok(new AuthResponse(token, user));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.usernameOrEmail(), request.password())
        );
        String username = authentication.getName();
        AppUser user = appUserService.findByUsernameOrEmail(username)
                .orElseThrow(() -> new IllegalStateException("Không tìm thấy người dùng sau khi đăng nhập"));
        UserDTO dto = appUserService.toDto(user);
        String token = jwtService.generateToken(dto.username(), Map.of("roles", dto.roles()));
        return ResponseEntity.ok(new AuthResponse(token, dto));
    }
}

