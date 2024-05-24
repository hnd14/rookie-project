package com.example.project.users.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.project.users.entities.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {

}
