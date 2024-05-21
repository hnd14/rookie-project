package com.example.project.users.entities;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.SecondaryTable;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "users")
@SecondaryTable(name = "user_details", pkJoinColumns = @PrimaryKeyJoinColumn(name = "users_id"))
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails{
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(unique = true, nullable = false, length = 100)
    private String username;

    @Column(unique = true, length = 100)
    private String email;
    
    @Column(length = 2500)
    private String password;

    @Embedded
    private UserInfos userInfos;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "users_roles",
        joinColumns = @JoinColumn(name = "users_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "role_name", referencedColumnName = "role_name")
    )
    private Set<Role> roles;


    @Override
    public boolean isAccountNonExpired () {
        return true;
    }

    @Override
    public boolean isAccountNonLocked () {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired () {
        return true;
    }

    @Override
    public boolean isEnabled () {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities () {
        if ( this.roles != null ) {
            return this.roles.stream().map(e -> new SimpleGrantedAuthority(e.getRoleName())).toList();
        }
        return Collections.emptyList();
    }
}
