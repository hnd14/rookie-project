package com.example.project.products.services;


import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.example.project.products.dto.Requests.GetCategoriesDto;
import com.example.project.products.entities.Category;

@Service
public interface CategoryService {
    Category getCategoryById(Long id);
    Page<Category> findAllCategories(GetCategoriesDto dto);
}
