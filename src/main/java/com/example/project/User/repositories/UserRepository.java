package com.example.project.User.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.project.User.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

}
