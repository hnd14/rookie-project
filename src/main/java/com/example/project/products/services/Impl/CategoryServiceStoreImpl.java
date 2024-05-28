package com.example.project.products.services.Impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.project.products.dto.Responses.CategoryDto;
import com.example.project.products.mapper.CategoryMapper;
import com.example.project.products.services.CategoryService;
import com.example.project.products.services.CategoryServiceStore;

@Service
@Transactional(readOnly = true)
public class CategoryServiceStoreImpl implements CategoryServiceStore{
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CategoryMapper mapper;
    
    @Override
    public CategoryDto findCategoryById(Long id) {
        return mapper.toDto(categoryService.getCategoryById(id));
    }

    @Override
    public List<CategoryDto> findCategoryByName(String name) {
        return categoryService.findCategoryByName(name).stream().map(mapper::toDto).collect(Collectors.toList());    
    }

}
