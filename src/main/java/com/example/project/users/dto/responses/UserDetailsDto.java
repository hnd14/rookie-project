package com.example.project.users.dto.responses;

import java.time.LocalDateTime;

import com.example.project.users.entities.UserInfos;

public record UserDetailsDto(String username, String email, UserInfos infos, LocalDateTime createdTime,
        LocalDateTime updatedTime) {

}
