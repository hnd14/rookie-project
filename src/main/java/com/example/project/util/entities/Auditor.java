package com.example.project.util.entities;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@MappedSuperclass
public class Auditor {
    @CreatedDate
    LocalDateTime createdTime;

    @LastModifiedDate
    LocalDateTime updatedTime;

    @CreatedBy
    String createdBy;

    @LastModifiedBy
    String updatedBy;

    @PrePersist
    void createdAudit(){
        setCreatedTime();
        setCreatedBy();
    }

    void setCreatedTime(){
        this.createdTime = LocalDateTime.now(); 
    }

    void setCreatedBy(){
        String username = "Anonymous";
        System.out.println("Hello");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null 
            || !authentication.isAuthenticated()
            || authentication instanceof AnonymousAuthenticationToken) {
            username = "Anonymous";
        }
        username = authentication.getPrincipal().toString();
        this.createdBy = username;
    }

    @PreUpdate
    void updateAudit(){
        setUpdatedBy();
        setUpdatedTime();
    }
    
    void setUpdatedTime(){
        this.updatedTime = LocalDateTime.now(); 
    }

    
    void setUpdatedBy(){
        String username;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null 
            || !authentication.isAuthenticated()
            || authentication instanceof AnonymousAuthenticationToken) {
            username = "Anonymous";
        }
        username = authentication.getPrincipal().toString();
        this.updatedBy = username;
    }
}
