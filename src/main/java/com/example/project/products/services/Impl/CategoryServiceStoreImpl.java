package com.example.project.products.services.Impl;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.project.products.dto.Responses.CategoryDto;
import com.example.project.products.mapper.CategoryMapper;
import com.example.project.products.services.CategoryService;
import com.example.project.products.services.CategoryServiceStore;
import com.example.project.util.dto.requests.PagingDto;
import com.example.project.util.dto.response.PagedDto;

@Service
@Transactional(readOnly = true)
public class CategoryServiceStoreImpl implements CategoryServiceStore {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CategoryMapper mapper;

    @Override
    public CategoryDto findCategoryById(Long id) {
        return mapper.toDto(categoryService.getCategoryById(id));
    }

    @Override
    public PagedDto<CategoryDto> findAllCategories(PagingDto dto) {
        var categories = categoryService.findAllCategories(dto);
        var content = categories.getContent().stream().map(mapper::toDto).collect(Collectors.toList());
        return new PagedDto<>(content, categories.getTotalPages(), categories.getNumber());
    }

}
