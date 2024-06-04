package com.example.project.products.dto.Requests;

import java.util.List;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record PostNewProductDto(@NotBlank String name, String desc, @Min(value = 0) Long stock,
                @Min(value = 0) Double salePrice,
                List<Long> categoriesId, Boolean isFeatured) {

}
