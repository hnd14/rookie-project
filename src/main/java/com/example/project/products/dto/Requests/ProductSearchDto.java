package com.example.project.products.dto.Requests;

import java.util.Optional;


public record ProductSearchDto(String name,
  Long categoriesId,
  Double minPrice, 
  Double maxPrice,
  Optional<String> sortBy,
  Optional<String> direction, 
  Optional<Integer> pageSize,
  Optional<Integer> pageNumber,
  Optional<Boolean> isFeatured) {}
