package com.example.project.products.dto.Requests;

import java.util.Optional;

public record GetCategoriesDto(Optional<String> sortBy,
  Optional<String> direction, 
  Optional<Integer> pageSize,
  Optional<Integer> pageNumber) {
} 
