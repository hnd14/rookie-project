package com.example.project.users.services.Impl;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
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
import com.example.project.users.dto.requests.UpdateUserPasswordDto;
import com.example.project.users.dto.responses.LoginResponseDto;
import com.example.project.users.dto.responses.UserAdminDto;
import com.example.project.users.dto.responses.UserDetailsDto;
import com.example.project.users.dto.responses.UserReturnDto;
import com.example.project.users.entities.Role;
import com.example.project.users.entities.User;
import com.example.project.users.repositories.RoleRepository;
import com.example.project.users.repositories.UserRepository;
import com.example.project.users.services.UserService;
import com.example.project.util.dto.requests.PagingDto;
import com.example.project.util.dto.response.PagedDto;
import com.example.project.util.exceptions.NotFoundException;

import jakarta.validation.ValidationException;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private static final String DEFAULT_SORT_BY = "id";
    private static final Integer DEFAULT_PAGE_SIZE = 10;
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
    public UserReturnDto createNewAdminUser(CreateNewAdminDto dto) {
        User newAdmin = new User();
        newAdmin.setUsername(dto.username());
        newAdmin.setPassword(passwordEncoder.encode(dto.password()));
        Set<Role> adminRoles = new HashSet<>();
        adminRoles.add(roleRepository.findById("ROLE_ADMIN").orElse(new Role("ROLE_ADMIN", null)));
        newAdmin.setRoles(adminRoles);
        repository.save(newAdmin);
        return new UserReturnDto(newAdmin.getUsername(), newAdmin.getEmail());
    }

    @Override
    @Transactional
    public UserReturnDto signUp(CustomerSignUpDto dto) {
        if (repository.findOneByUsername(dto.username()).isPresent()) {
            throw new ValidationException("Username is already used");
        }
        if (repository.findOneByEmail(dto.email()).isPresent()) {
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
    public UserReturnDto getUserById(Long id) {
        User res = repository.findById(id).orElseThrow(NotFoundException::new);
        return new UserReturnDto(res.getUsername(), res.getEmail());
    }

    @Override
    @Transactional
    public UserReturnDto updateUserInfo(String username, UpdateUserInfoDto dto) {
        User userToUpdate = repository.findOneByUsername(username).orElseThrow(NotFoundException::new);
        userToUpdate.setPassword(passwordEncoder.encode(dto.rawPassword()));
        userToUpdate.setUserInfos(dto.details());
        repository.save(userToUpdate);
        return new UserReturnDto(userToUpdate.getUsername(), userToUpdate.getEmail());
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        repository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username.isBlank()) {
            return null;
        }
        return repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
    }

    @Override
    public LoginResponseDto signIn(SignInDto dto) {
        var userNamePassWord = new UsernamePasswordAuthenticationToken(dto.username(), dto.rawPassword());
        var authUser = authenticationManager.authenticate(userNamePassWord);
        var accessToken = tokenProvider.generateAccessToken((User) authUser.getPrincipal());
        return new LoginResponseDto(Optional.of(accessToken),
                authUser.getName(),
                authUser.getAuthorities().stream().map((auth) -> auth.getAuthority()).collect(Collectors.toList()),
                true);
    }

    @Override
    public LoginResponseDto verify() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            return new LoginResponseDto(null, null, null, false);
        }

        return new LoginResponseDto(Optional.empty(),
                authentication.getName(),
                authentication.getAuthorities().stream().map((auth) -> (String) auth.getAuthority())
                        .collect(Collectors.toList()),
                true);
    }

    @Override
    public PagedDto<UserAdminDto> getUsersLists(PagingDto dto) {
        var sortBy = dto.sortBy().orElse(DEFAULT_SORT_BY);
        String sortDir = dto.direction().orElse("DESC");
        Sort.Direction direction = sortDir.equals("ASC") ? Direction.ASC : Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);

        Integer pageSize = dto.pageSize().orElse(DEFAULT_PAGE_SIZE);
        Integer pageNumber = dto.pageNumber().orElse(1);
        var page = PageRequest.of(pageNumber - 1, pageSize, sort);
        var content = repository.findAll(page);
        return new PagedDto<UserAdminDto>(content.getContent().stream()
                .map((user) -> (new UserAdminDto(user.getUsername(), user.getEmail(),
                        String.valueOf(user.getUpdatedTime()),
                        String.valueOf(user.getCreatedTime()),
                        user.getRoles().stream().map(role -> role.getRoleName()).collect(Collectors.toList()))))
                .collect(Collectors.toList()), content.getTotalPages(), content.getNumber());
    }

    @Override
    public UserDetailsDto getCurrentUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            return new UserDetailsDto(null, null, null);
        }
        var username = authentication.getName();
        var user = repository.findOneByUsername(username).orElse(new User());
        return new UserDetailsDto(user.getUsername(), user.getEmail(), user.getUserInfos(), user.getCreatedTime(),
                user.getUpdatedTime());
    }

    @Override
    @Transactional
    public UserReturnDto updatePassword(String username, UpdateUserPasswordDto dto) {
        User userToUpdate = repository.findOneByUsername(username).orElseThrow(NotFoundException::new);
        if (!passwordEncoder.matches(dto.oldPassword(), userToUpdate.getPassword())) {
            throw new SecurityException("Your pass word does not match!");
        }
        userToUpdate.setPassword(passwordEncoder.encode(dto.newPassword()));
        repository.save(userToUpdate);
        return new UserReturnDto(userToUpdate.getUsername(), userToUpdate.getEmail());
    }

}
