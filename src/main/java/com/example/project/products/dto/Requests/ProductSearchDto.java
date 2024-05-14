package com.example.project.products.dto.Requests;

import java.util.List;

public record ProductSearchDto(String name, List<Long> categoriesId, Double minPrice, Double maxPrice) {}
