package com.example.project.products.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.project.products.dto.Responses.CategoryDto;

@Service
public interface CategoryServiceStore {
    CategoryDto findCategoryById(Long id);
    List<CategoryDto> findCategoryByName(String name);
}
