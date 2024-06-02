package com.example.project.products.dto.Responses;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductCustomerDto {
    private Long id;
    private String name;
    private String desc;
    private Double salePrice; 
    private String thumbnailUrl;
    private Double averageScore;
    private List<String> categories;
    private List<String> imagesUrl;
}
