package com.example.project.products.services.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.project.products.entities.Category;
import com.example.project.products.exceptions.CategoryNotFoundException;
import com.example.project.products.repositories.CategoryRepository;
import com.example.project.products.services.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository repo;

    @Override
    public Category getCategoryById(Long id) {
        return repo.findById(id).orElseThrow(CategoryNotFoundException::new);
    }

    @Override
    public List<Category> findCategoryByName(String name) {
        return repo.findByNameContains(name);
    }

}
