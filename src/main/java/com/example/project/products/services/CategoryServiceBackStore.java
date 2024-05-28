package com.example.project.products.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.project.products.dto.Requests.PostNewCategoryDto;
import com.example.project.products.dto.Requests.UpdateCategoryDto;
import com.example.project.products.dto.Responses.CategoryAdminDto;

@Service
public interface CategoryServiceBackStore {
    CategoryAdminDto findCategoryById(Long id);
    List<CategoryAdminDto> findCategoryByName(String name);
    CategoryAdminDto createNew(PostNewCategoryDto dto);
    CategoryAdminDto update(Long id, UpdateCategoryDto dto);
    void deleteCategory(Long id);
}
