package com.example.project.users.services;


import org.springframework.security.core.userdetails.UserDetailsService;

import com.example.project.users.dto.requests.CreateNewAdminDto;
import com.example.project.users.dto.requests.CustomerSignUpDto;
import com.example.project.users.dto.requests.UpdateUserInfoDto;
import com.example.project.users.dto.responses.UserReturnDto;

public interface UserService extends UserDetailsService{

    UserReturnDto createNewAdminUser(CreateNewAdminDto dto);

    UserReturnDto signUp(CustomerSignUpDto dto);

    UserReturnDto getUserById(Long id);

    UserReturnDto updateUserInfo(Long id, UpdateUserInfoDto dto);

    void deleteUser(Long id);

}