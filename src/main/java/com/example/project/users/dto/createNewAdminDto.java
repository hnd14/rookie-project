package com.example.project.users.dto;

import jakarta.validation.constraints.NotBlank;

public record createNewAdminDto(@NotBlank String username, @NotBlank String password) {

}
