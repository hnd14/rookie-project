package com.example.project.users.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import com.example.project.users.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<UserDetails> findUserByUsername(String username);
    Optional<User> findOneByUsername(String username);
    Optional<User> findOneByEmail(String email);
    
}
