package com.example.project.users.dto.requests;

import jakarta.validation.constraints.NotBlank;

public record SignInDto(@NotBlank String username, @NotBlank String rawPassword) {

}
