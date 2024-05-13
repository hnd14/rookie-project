package com.example.project.products.dto.Requests;

public record ProductSearchDto(String name, Long[] categoriesId, Double minPrice, Double maxPrice) {}
