package com.example.project.users.dataLoaders;



import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.project.users.entities.Role;
import com.example.project.users.entities.User;
import com.example.project.users.repositories.RoleRepository;
import com.example.project.users.repositories.UserRepository;

@Component
public class InitUserLoader implements CommandLineRunner{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String ...args){
        // Load init roles
        Role roleAdmin = new Role("ROLE_ADMIN", null);
        Role roleCustomer = new Role("ROLE_CUSTOMER", null);
        roleRepository.save(roleAdmin);
        roleRepository.save(roleCustomer);
        // Init admin user 
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin"));
        Set<Role> adminRoles = new HashSet<>();
        adminRoles.add(roleAdmin);
        admin.setRoles(adminRoles);
        userRepository.save(admin);
    }
}
