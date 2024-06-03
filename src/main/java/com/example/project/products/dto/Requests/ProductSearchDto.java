package com.example.project.products.dto.Requests;

import java.util.Optional;

public record ProductSearchDto(Optional<String> name,
    Optional<Long> categoriesId,
    Optional<Double> minPrice,
    Optional<Double> maxPrice,
    Optional<String> sortBy,
    Optional<String> direction,
    Optional<Integer> pageSize,
    Optional<Integer> pageNumber,
    Optional<Boolean> isFeatured) {
}
