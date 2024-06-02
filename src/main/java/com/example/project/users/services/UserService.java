package com.example.project.users.services;


import org.springframework.security.core.userdetails.UserDetailsService;

import com.example.project.users.dto.requests.CreateNewAdminDto;
import com.example.project.users.dto.requests.CustomerSignUpDto;
import com.example.project.users.dto.requests.SignInDto;
import com.example.project.users.dto.requests.UpdateUserInfoDto;
import com.example.project.users.dto.responses.LoginResponseDto;
import com.example.project.users.dto.responses.UserAdminDto;
import com.example.project.users.dto.responses.UserReturnDto;
import com.example.project.util.dto.requests.PagingDto;
import com.example.project.util.dto.response.PagedDto;

public interface UserService extends UserDetailsService{

    UserReturnDto createNewAdminUser(CreateNewAdminDto dto);

    UserReturnDto signUp(CustomerSignUpDto dto);

    UserReturnDto getUserById(Long id);

    UserReturnDto updateUserInfo(String username, UpdateUserInfoDto dto);

    void deleteUser(Long id);

    LoginResponseDto signIn(SignInDto dto);

    LoginResponseDto verify();

    PagedDto<UserAdminDto> getUsersLists(PagingDto dto);

}