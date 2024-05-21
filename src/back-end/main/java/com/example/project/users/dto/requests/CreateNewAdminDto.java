package com.example.project.users.dto.requests;

import jakarta.validation.constraints.NotBlank;

public record CreateNewAdminDto(@NotBlank String username, @NotBlank String password, @NotBlank String email) {

}
