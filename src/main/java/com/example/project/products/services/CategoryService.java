package com.example.project.products.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.project.products.entities.Category;

@Service
public interface CategoryService {
    Category getCategoryById(Long id);
    List<Category> findCategoryByName(String name);
}
