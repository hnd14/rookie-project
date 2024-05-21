package com.example.project.users.dto.requests;

import com.example.project.users.entities.UserInfos;

import jakarta.validation.constraints.NotBlank;

public record CustomerSignUpDto(@NotBlank String username, @NotBlank String password, 
@NotBlank String email, 
UserInfos userDetails) {

}
