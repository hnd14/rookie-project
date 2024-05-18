package com.example.project.users.services.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.project.users.dto.requests.CreateNewAdminDto;
import com.example.project.users.dto.requests.CustomerSignUpDto;
import com.example.project.users.dto.responses.UserReturnDto;
import com.example.project.users.entities.User;
import com.example.project.users.repositories.UserRepository;
import com.example.project.users.services.UserService;
import com.example.project.util.entities.PasswordEncoder;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository repository;

    @Transactional
    public UserReturnDto createNewAdminUser(CreateNewAdminDto dto){
        User newAdmin = new User();
        newAdmin.setUsername(dto.username());
        newAdmin.setEmail(dto.email());
        newAdmin.setPassword(passwordEncoder.encode(dto.password()));
        repository.save(newAdmin);
        return new UserReturnDto(newAdmin.getUsername(), newAdmin.getEmail());
    }

    @Transactional
    public UserReturnDto signUp(CustomerSignUpDto dto){
        User newAdmin = new User();
        newAdmin.setUsername(dto.username());
        newAdmin.setEmail(dto.email());
        newAdmin.setPassword(passwordEncoder.encode(dto.password()));
        repository.save(newAdmin);
        return new UserReturnDto(newAdmin.getUsername(), newAdmin.getEmail());
    }
}
