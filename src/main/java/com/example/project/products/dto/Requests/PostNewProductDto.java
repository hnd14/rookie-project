package com.example.project.products.dto.Requests;

import java.util.List;

import jakarta.validation.constraints.NotBlank;

public record PostNewProductDto(@NotBlank String name, String desc, Long stock, Double SalePrice, List<Double> categoriesId) {

}
