package com.example.project.products.dto.Responses;

import java.time.LocalDateTime;

public record CategoryAdminDto(Long id,
 String name, 
 String desc, 
 String createdBy, 
 String updatedBy,
 LocalDateTime createdTime,
 LocalDateTime updatedTime) {
}
