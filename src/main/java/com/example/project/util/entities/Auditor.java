package com.example.project.util.entities;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Embeddable
public class Auditor {
    @CreationTimestamp
    LocalDateTime createdTime;

    @UpdateTimestamp
    LocalDateTime updatedTime;
}
