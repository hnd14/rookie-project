package com.example.project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.project.users.dto.requests.SignInDto;
import com.example.project.users.dto.responses.JwtToken;
import com.example.project.users.services.UserService;


import jakarta.validation.Valid;

@RequestMapping("/auth")
@RestController
public class AuthController extends V1Rest {
    @Autowired
    private UserService userService;
    
    @PostMapping("/signin")
    public JwtToken signIn(@RequestBody @Valid SignInDto dto){
        return userService.signIn(dto);
    } 
}
