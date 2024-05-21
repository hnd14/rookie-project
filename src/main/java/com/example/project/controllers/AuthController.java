package com.example.project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


import com.example.project.users.dto.requests.SignInDto;
import com.example.project.users.dto.responses.JwtToken;
import com.example.project.users.services.UserService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;

@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserService userService;
    
    @PostMapping("/signin")
    public JwtToken signIn(@RequestBody @Valid SignInDto dto){
        return userService.signIn(dto);
    } 
}
