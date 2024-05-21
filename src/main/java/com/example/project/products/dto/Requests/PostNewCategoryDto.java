package com.example.project.products.dto.Requests;

import jakarta.validation.constraints.NotBlank;

public record PostNewCategoryDto(@NotBlank String name, String desc) {

}
