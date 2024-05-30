package com.example.project.products.services;

import org.springframework.stereotype.Service;

import com.example.project.products.dto.Responses.CategoryDto;
import com.example.project.products.dto.Responses.PagedDto;
import com.example.project.util.entities.PagingDto;

@Service
public interface CategoryServiceStore {
    CategoryDto findCategoryById(Long id);
    PagedDto<CategoryDto> findAllCategories(PagingDto dto);
}
