package com.example.project.users.dto.responses;

public record LoginResponseDto(String accessToken, Boolean isAdmin, Boolean isCustomer) {
}
