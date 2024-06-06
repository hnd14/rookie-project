package com.example.project.products.services.Impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.example.project.products.dto.Requests.PostNewCategoryDto;
import com.example.project.products.entities.Category;
import com.example.project.products.entities.ProductCategory;
import com.example.project.products.exceptions.CategoryNotFoundException;
import com.example.project.products.mapper.CategoryMapper;
import com.example.project.products.repositories.CategoryRepository;
import com.example.project.util.dto.requests.PagingDto;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(Lifecycle.PER_CLASS)
@Transactional
public class CategoryServiceImplTest {
    @Autowired
    CategoryServiceImpl service;
    @Autowired
    CategoryMapper mapper;
    @Autowired
    CategoryRepository repo;
    PostNewCategoryDto postDto;
    private final String CATEGORY_IN_DB_NAME = "new category";
    private Category newCat;
    private PagingDto defaultPage = new PagingDto(Optional.of("name"), Optional.of("ASC"), Optional.of(10),
            Optional.of(1));

    @BeforeAll
    void setUpData() {
        repo.deleteAll();
        newCat = new Category(Long.valueOf(1), CATEGORY_IN_DB_NAME, "", new ArrayList<ProductCategory>());

    }

    @BeforeEach
    void setUp() {
        postDto = new PostNewCategoryDto(null, null);
        repo.save(newCat);
    }

    @AfterEach
    void tearDown() {
        repo.delete(newCat);
    }

    @Test
    void testFindAllCategories_whenGivenEmpty_shouldUseDefault() {
        // set up
        var emptyDto = new PagingDto(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
        var expected = service.findAllCategories(defaultPage);
        // run
        var result = service.findAllCategories(emptyDto);
        // assert
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void testFindAllCategories_whenGivenDescSort_shouldReturn() {
        // set up
        var inputDto = new PagingDto(Optional.empty(), Optional.of("DESC"), Optional.empty(), Optional.empty());
        // run
        var result = service.findAllCategories(inputDto);
        // assert
        assertThat(result).isInstanceOf(Page.class);
    }

    @Test
    void testFindAllCategories_whenGivenPageIndex0_shouldThrow() {
        // set up
        var dto = new PagingDto(Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(0));
        // run
        assertThrows(IllegalArgumentException.class, () -> {
            service.findAllCategories(dto);
        });
    }

    @Test
    void testFindAllCategories_whenGivenPageIndexNegative_shouldThrow() {
        // set up
        var dto = new PagingDto(Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(-1));
        // run
        assertThrows(IllegalArgumentException.class, () -> {
            service.findAllCategories(dto);
        });
    }

    @Test
    void testFindAllCategories_whenGivenPageSize0_shouldThrow() {
        // set up
        var dto = new PagingDto(Optional.empty(), Optional.empty(), Optional.of(0), Optional.empty());
        // run
        assertThrows(IllegalArgumentException.class, () -> {
            service.findAllCategories(dto);
        });
    }

    @Test
    void testFindAllCategories_whenGivenPageSizeNegative_shouldThrow() {
        // set up
        var dto = new PagingDto(Optional.empty(), Optional.empty(), Optional.of(-1), Optional.empty());
        // run
        assertThrows(IllegalArgumentException.class, () -> {
            service.findAllCategories(dto);
        });
    }

    @Test
    void testFindAllCategories_whenGivenBadSortBy_shouldThrow() {
        // set up
        var dto = new PagingDto(Optional.of("user"), Optional.empty(), Optional.empty(), Optional.empty());
        // run
        assertThrows(PropertyReferenceException.class, () -> {
            service.findAllCategories(dto);
        });
    }

    @Test
    void testGetCategoryById_whenGivenCorrectId_shouldFindTheCategoryAndTurnToDto() {
        // set up
        var expected = repo.findAll().getFirst();
        // run
        var res = service.getCategoryById(expected.getId());
        // assert
        assertThat(res).isEqualTo(expected);
    }

    @Test
    void testGetCategoryById_whenGivenWrongId_shouldThrow() {
        // set up
        var id = newCat.getId() - 1;
        // run
        assertThrows(CategoryNotFoundException.class, () -> {
            service.getCategoryById(id);
        });
    }

}
