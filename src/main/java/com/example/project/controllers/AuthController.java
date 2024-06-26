package com.example.project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.project.users.dto.requests.SignInDto;
import com.example.project.users.dto.requests.UpdateUserInfoDto;
import com.example.project.users.dto.requests.UpdateUserPasswordDto;
import com.example.project.users.dto.responses.LoginResponseDto;
import com.example.project.users.dto.responses.UserDetailsDto;
import com.example.project.users.dto.responses.UserReturnDto;
import com.example.project.users.services.UserService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RequestMapping("/auth")
@RestController
public class AuthController extends V1Rest {
    @Autowired
    private UserService userService;

    @PostMapping("/sign-in")
    public LoginResponseDto signIn(@RequestBody @Valid SignInDto dto, HttpServletResponse response) {
        var jwtToken = userService.signIn(dto);
        Cookie cookie = new Cookie("accessToken", jwtToken.accessToken().get());
        cookie.setPath("/");
        response.addCookie(cookie);
        return jwtToken;
    }

    @GetMapping("/log-out")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void signOut(HttpServletResponse response) {
        Cookie cookie = new Cookie("accessToken", null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    @GetMapping("/verify")
    public LoginResponseDto verify() {
        return userService.verify();
    }

    @GetMapping("/me")
    public UserDetailsDto getCurrentUserDetails() {
        return userService.getCurrentUser();
    }

    @PutMapping("/me")
    public UserReturnDto updateUserInfo(@RequestBody UpdateUserInfoDto dto) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new SecurityException("You need to log in to use this function.");
        }
        var username = authentication.getPrincipal().toString();
        return userService.updateUserInfo(username, dto);
    }

    @PutMapping("/me/password")
    public UserReturnDto updateUserPassword(@RequestBody UpdateUserPasswordDto dto) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new SecurityException("You need to log in to use this function.");
        }
        var username = authentication.getPrincipal().toString();
        return userService.updatePassword(username, dto);
    }
}
