package com.codeforge.judge.web.rest;

import com.codeforge.judge.domain.AppUser;
import com.codeforge.judge.service.AppUserService;
import com.codeforge.judge.service.CurrentUserService;
import com.codeforge.judge.service.dto.UserDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/me")
public class UserController {

    private final CurrentUserService currentUserService;
    private final AppUserService appUserService;

    public UserController(CurrentUserService currentUserService, AppUserService appUserService) {
        this.currentUserService = currentUserService;
        this.appUserService = appUserService;
    }

    @GetMapping
    public UserDTO me() {
        AppUser user = currentUserService.getCurrentUser();
        return appUserService.toDto(user);
    }
}

