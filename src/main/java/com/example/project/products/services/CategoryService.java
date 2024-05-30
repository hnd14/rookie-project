package com.example.project.products.services;


import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.example.project.products.entities.Category;
import com.example.project.util.entities.PagingDto;

@Service
public interface CategoryService {
    Category getCategoryById(Long id);
    Page<Category> findAllCategories(PagingDto dto);
}
