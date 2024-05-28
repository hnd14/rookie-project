package com.example.project.products.services.Impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.project.products.dto.Requests.GetCategoriesDto;
import com.example.project.products.entities.Category;
import com.example.project.products.exceptions.CategoryNotFoundException;
import com.example.project.products.repositories.CategoryRepository;
import com.example.project.products.services.CategoryService;

@Service
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {
    
    @Autowired
    private CategoryRepository repo;
    private final Integer DEFAULT_PAGE_SIZE = 10;
    private final String DEFAULT_SORT_BY = "name";

    @Override
    public Category getCategoryById(Long id) {
        return repo.findById(id).orElseThrow(CategoryNotFoundException::new);
    }

    @Override
    public Page<Category> findAllCategories(GetCategoriesDto dto) {
        var sortBy = dto.sortBy().orElse(DEFAULT_SORT_BY);
        String sortDir =dto.direction().orElse("ASC");
        Sort.Direction direction = sortDir.equals("DESC")?Direction.DESC:Direction.ASC;
        Sort sort = Sort.by(direction, sortBy);

        Integer pageSize = dto.pageSize().orElse(DEFAULT_PAGE_SIZE);
        Integer pageNumber = dto.pageNumber().orElse(1);
        var page = PageRequest.of(pageNumber-1, pageSize, sort);
        return repo.findAll(page);
    }

}
