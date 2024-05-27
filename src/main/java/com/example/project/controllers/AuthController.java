package com.example.project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.project.users.dto.requests.SignInDto;
import com.example.project.users.dto.responses.LoginResponseDto;
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
    public LoginResponseDto signIn(@RequestBody @Valid SignInDto dto, HttpServletResponse response){
        var jwtToken = userService.signIn(dto);
        Cookie cookie = new Cookie("accessToken", jwtToken.accessToken());
        cookie.setPath("/"); 
        response.addCookie(cookie);
        return jwtToken;
    } 
}
