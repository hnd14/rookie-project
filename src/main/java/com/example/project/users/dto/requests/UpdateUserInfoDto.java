package com.example.project.users.dto.requests;

import com.example.project.users.entities.UserDetails;

public record UpdateUserInfoDto(String rawPassword, UserDetails details) {

}
