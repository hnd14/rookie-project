package com.example.project.products.dto.Responses;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ProductDetailsAdminDto {
    private Long id;
    private String name;
    private String desc;
    private Double salePrice;
    private Long stock;
    private Boolean isFeatured;
    private String thumbnailUrl;
    private String createdBy;
    private String updatedBy;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
    private List<SimpleCategoryDto> categoriesInfo;
    private List<String> imagesUrl;

}
