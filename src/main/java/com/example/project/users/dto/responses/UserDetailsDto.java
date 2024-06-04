package com.example.project.users.dto.responses;

import com.example.project.users.entities.UserInfos;

public record UserDetailsDto(String username, String email, UserInfos infos) {

}
