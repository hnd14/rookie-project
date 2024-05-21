package com.example.project.users.dto.requests;

import com.example.project.users.entities.UserInfos;

public record UpdateUserInfoDto(String rawPassword, UserInfos details) {

}
