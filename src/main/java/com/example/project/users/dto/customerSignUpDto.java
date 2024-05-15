package com.example.project.users.dto;

import com.example.project.users.entities.UserDetails;

import jakarta.validation.constraints.NotBlank;

public record customerSignUpDto(@NotBlank String username, @NotBlank String password, UserDetails userDetails) {

}
