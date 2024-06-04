package com.example.project.products.dto.Responses;

import java.time.LocalDateTime;

public record ProductAdminDto(Long id,
                String name,
                String desc,
                Double salePrice,
                Long stock,
                Double avgRating,
                Boolean isFeatured,
                String createdBy,
                String updatedBy,
                LocalDateTime createdTime,
                LocalDateTime updatedTime) {

}
