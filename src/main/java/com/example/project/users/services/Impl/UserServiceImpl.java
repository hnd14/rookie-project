package com.example.project.users.services.Impl;


import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.project.jwt.TokenProvider;
import com.example.project.users.dto.requests.CreateNewAdminDto;
import com.example.project.users.dto.requests.CustomerSignUpDto;
import com.example.project.users.dto.requests.SignInDto;
import com.example.project.users.dto.requests.UpdateUserInfoDto;
import com.example.project.users.dto.responses.LoginResponseDto;
import com.example.project.users.dto.responses.UserReturnDto;
import com.example.project.users.entities.Role;
import com.example.project.users.entities.User;
import com.example.project.users.repositories.RoleRepository;
import com.example.project.users.repositories.UserRepository;
import com.example.project.users.services.UserService;
import com.example.project.util.exceptions.NotFoundException;

import jakarta.validation.ValidationException;



@Service
@Transactional(readOnly=true)
public class UserServiceImpl implements UserService  {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository repository;
    @Lazy
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private RoleRepository roleRepository;

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
        Set<Role> userRoles = new HashSet<>();
        userRoles.add(roleRepository.findById("ROLE_CUSTOMER").orElse(new Role("ROLE_CUSTOMER", null)));
        newUser.setRoles(userRoles);
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
    public UserReturnDto updateUserInfo(String username,UpdateUserInfoDto dto){
        User userToUpdate = repository.findOneByUsername(username).orElseThrow(NotFoundException::new);
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
        if (username.isBlank()){
            return null;
        }
        return repository.findByUsername(username).orElseThrow(()->new UsernameNotFoundException("Username not found"));
    }

    @Override
    public LoginResponseDto signIn(SignInDto dto) {
        var userNamePassWord = new UsernamePasswordAuthenticationToken(dto.username(), dto.rawPassword());
        var authUser = authenticationManager.authenticate(userNamePassWord);
        var accessToken = tokenProvider.generateAccessToken((User) authUser.getPrincipal());
        // check if authenticated user is admin
        Boolean isAdmin = authUser.getAuthorities().stream()
        .map((authority) -> authority.getAuthority())
        .anyMatch((authority)->authority.equals("ROLE_ADMIN"));
        // check if authenticated user is customer
        Boolean isCustomer = authUser.getAuthorities().stream()
        .map((authority) -> authority.getAuthority())
        .anyMatch((authority)->authority.equals("ROLE_CUSTOMER"));
        return new LoginResponseDto(accessToken, isAdmin, isCustomer);
    }
    
}
