package com.example.project.users.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.project.users.dto.responses.UserAdminDto;
import com.example.project.users.services.UserService;
import com.example.project.util.dto.requests.PagingDto;
import com.example.project.util.dto.response.PagedDto;

@RestController
@RequestMapping("/store-back/users")
public class UserController {
    @Autowired
    private UserService service;

    @GetMapping
    public PagedDto<UserAdminDto> getAllUser(PagingDto dto){
        return service.getUsersLists(dto); 
    }
}
