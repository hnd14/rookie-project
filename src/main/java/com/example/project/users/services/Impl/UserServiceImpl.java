package com.example.project.users.services.Impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.project.users.dto.requests.CreateNewAdminDto;
import com.example.project.users.dto.requests.CustomerSignUpDto;
import com.example.project.users.dto.requests.UpdateUserInfoDto;
import com.example.project.users.dto.responses.UserReturnDto;
import com.example.project.users.entities.User;
import com.example.project.users.repositories.UserRepository;
import com.example.project.users.services.UserService;
import com.example.project.util.entities.NotFoundException;


import jakarta.validation.ValidationException;



@Service
@Transactional(readOnly=true)
public class UserServiceImpl implements UserService  {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository repository;

    @Override
    @Transactional
    public UserReturnDto createNewAdminUser(CreateNewAdminDto dto){
        User newAdmin = new User();
        newAdmin.setUsername(dto.username());
        newAdmin.setEmail(dto.email());
        newAdmin.setPassword(passwordEncoder.encode(dto.password()));
        repository.save(newAdmin);
        return new UserReturnDto(newAdmin.getUsername(), newAdmin.getEmail());
    }

    @Override
    @Transactional
    public UserReturnDto signUp(CustomerSignUpDto dto){
        if (repository.findOneByUsername(dto.username()).isPresent()){
            throw new ValidationException("Username is already used");
        }
        if (repository.findOneByEmail(dto.email()).isPresent()){
            throw new ValidationException("Email is already used");
        }
        User newUser = new User();
        newUser.setUsername(dto.username());
        newUser.setEmail(dto.email());
        newUser.setPassword(passwordEncoder.encode(dto.password()));
        repository.save(newUser);
        return new UserReturnDto(newUser.getUsername(), newUser.getEmail());
    }

    @Override
    public UserReturnDto getUserById(Long id){
        User res = repository.findById(id).orElseThrow(NotFoundException::new);
        return new UserReturnDto(res.getUsername(), res.getEmail());
    }

    @Override
    @Transactional
    public UserReturnDto updateUserInfo(Long id,UpdateUserInfoDto dto){
        User userToUpdate = repository.findById(id).orElseThrow(NotFoundException::new);
        userToUpdate.setPassword(passwordEncoder.encode(dto.rawPassword()));
        userToUpdate.setUserInfos(dto.details());
        repository.save(userToUpdate);
        return new UserReturnDto(userToUpdate.getUsername(), userToUpdate.getEmail());
    }

    @Override
    @Transactional
    public void deleteUser(Long id){
        repository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findUserByUsername(username).orElseThrow(()->new UsernameNotFoundException("Username not found"));
    }
    
}
