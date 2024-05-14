package com.example.project.products.dto.Requests;

import java.util.List;

public record UpdateProductDto(String desc, Long stock, Double salePrice, List<Long> categoriesId, Boolean isFeatured) {

}
