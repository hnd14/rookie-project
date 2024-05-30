package com.example.project.users.dto.responses;

import java.util.List;
import java.util.Optional;

public record LoginResponseDto(Optional<String> accessToken, String username, List<String> roles, Boolean isAuthenticated) {
}
