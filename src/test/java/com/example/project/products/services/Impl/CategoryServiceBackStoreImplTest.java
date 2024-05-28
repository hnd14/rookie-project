package com.example.project.products.services.Impl;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;

import com.example.project.products.dto.Requests.PostNewCategoryDto;
import com.example.project.products.mapper.CategoryMapper;
import com.example.project.products.repositories.CategoryRepository;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;

import com.example.project.products.entities.Category;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(Lifecycle.PER_CLASS)
public class CategoryServiceBackStoreImplTest {
    @Autowired
    CategoryServiceBackStoreImpl service;
    @Autowired
    CategoryMapper mapper;
    @Autowired
    CategoryRepository repo;
    PostNewCategoryDto postDto;
    private final String CATEGORY_IN_DB_NAME = "new category";
    private final String CATEGORY_NOT_IN_DB_NAME = "yrogetac";
    static Category newCat;

    @BeforeAll
    void setUpData(){
        newCat = new Category();
        newCat.setName(CATEGORY_IN_DB_NAME);
        repo.save(newCat);
    }
    @BeforeEach
    void setUp(){
        postDto = new PostNewCategoryDto(null,null);
    }

    
    @Test
    void testCreateNew_successfully_shouldReturnDtoForTheCreatedCategory () {
        // set up
        postDto = new PostNewCategoryDto("test", "test description");
        // Run
        var result = service.createNew(postDto);
        // assert
        assertThat(result).hasFieldOrPropertyWithValue("name", "test");
        assertThat(result).hasFieldOrPropertyWithValue("desc", "test description");
        assertThat(result.id()).isNotNull();
    }

    @Test
    void testCreateNew_nullName_shouldThrowIllegalArgumentsException () {
        assertThrows(ConstraintViolationException.class, ()-> service.createNew(postDto));
    }

    @Test
    void testCreateNew_emptyName_shouldThrowIllegalArgumentsException () {
        // set up
        postDto = new PostNewCategoryDto("", "test description");
        // run and assert
        assertThrows(ValidationException.class, ()-> service.createNew(postDto));
    }

    @Test
    void testCategoryFindByName_whenQueriedFullName_shouldReturnListWithTheItemInside(){
        //set up
        var expectedDto = mapper.toAdminDto(newCat);
        //do work
        var result = service.findCategoryByName(CATEGORY_IN_DB_NAME);
        //result
        assertThat(result).contains(expectedDto);
    }

    @Test
    void testFindCategoryByName_whenQUeriedWithPartialName_shouldReturnListWithTheItemInside() {
        //set up
        var expectedDto = mapper.toAdminDto(newCat);
        //do work
        var result = service.findCategoryByName(CATEGORY_IN_DB_NAME.substring(0,3));
        //result
        assertThat(result).contains(expectedDto);
    }

    @Test
    void testCategoryFindByName_whenQueriedNameNotExist_shouldReturnEmptyList(){
        //do work
        var result = service.findCategoryByName(CATEGORY_NOT_IN_DB_NAME);
        //result
        assertThat(result).isEmpty();;
    }
}
