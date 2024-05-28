package com.example.project.products.services;

import org.springframework.stereotype.Service;

import com.example.project.products.dto.Requests.GetCategoriesDto;
import com.example.project.products.dto.Responses.CategoryDto;
import com.example.project.products.dto.Responses.PagedDto;

@Service
public interface CategoryServiceStore {
    CategoryDto findCategoryById(Long id);
    PagedDto<CategoryDto> findAllCategories(GetCategoriesDto dto);
}
