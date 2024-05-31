package com.example.project.products.services;


import org.springframework.stereotype.Service;

import com.example.project.products.dto.Requests.PostNewCategoryDto;
import com.example.project.products.dto.Requests.UpdateCategoryDto;
import com.example.project.products.dto.Responses.CategoryAdminDto;
import com.example.project.util.dto.requests.PagingDto;
import com.example.project.util.dto.response.PagedDto;

@Service
public interface CategoryServiceBackStore {
    CategoryAdminDto findCategoryById(Long id);
    PagedDto<CategoryAdminDto> findAllCategories(PagingDto dto);
    CategoryAdminDto createNew(PostNewCategoryDto dto);
    CategoryAdminDto update(Long id, UpdateCategoryDto dto);
    void deleteCategory(Long id);
}
