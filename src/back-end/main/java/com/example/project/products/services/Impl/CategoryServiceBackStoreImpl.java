package com.example.project.products.services.Impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.project.products.dto.Requests.PostNewCategoryDto;
import com.example.project.products.dto.Responses.CategoryDto;
import com.example.project.products.entities.Category;
import com.example.project.products.mapper.CategoryMapper;
import com.example.project.products.repositories.CategoryRepository;
import com.example.project.products.services.CategoryService;
import com.example.project.products.services.CategoryServiceBackStore;

import jakarta.validation.Valid;



@Service
@Transactional(readOnly = true)
public class CategoryServiceBackStoreImpl implements CategoryServiceBackStore {
    @Autowired
    private CategoryRepository repo;
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

    @Override
    @Transactional
    public CategoryDto createNew(@Valid PostNewCategoryDto dto) {
        Category newCategory = mapper.toNewCategory(dto);
        repo.save(newCategory);
        return mapper.toDto(newCategory);
    }

    @Transactional
    public void deleteCategory(Long id){
        repo.deleteById(id);
    }
}
