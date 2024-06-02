package com.example.project.users.dto.responses;

import java.util.List;

public record UserAdminDto(String username, String email, String updatedTime, String createdTime, List<String> roles) {
    
}
