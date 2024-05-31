package com.example.project.products.services;

import org.springframework.stereotype.Service;

import com.example.project.products.dto.Responses.CategoryDto;
import com.example.project.util.dto.requests.PagingDto;
import com.example.project.util.dto.response.PagedDto;

@Service
public interface CategoryServiceStore {
    CategoryDto findCategoryById(Long id);
    PagedDto<CategoryDto> findAllCategories(PagingDto dto);
}
